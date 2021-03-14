package com.jarrvis.ticketbooking.application.service;

import com.jarrvis.ticketbooking.domain.Reservation;
import com.jarrvis.ticketbooking.domain.ReservationRepository;
import com.jarrvis.ticketbooking.domain.ReservationStatus;
import com.jarrvis.ticketbooking.domain.Screening;
import com.jarrvis.ticketbooking.domain.ScreeningRepository;
import com.jarrvis.ticketbooking.domain.Ticket;
import com.jarrvis.ticketbooking.ui.dto.response.ReservationResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ScreeningRepository screeningRepository) {

        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
    }

    /**
     * @param screeningId id of screening to be reserved
     * @param name        name of the customer
     * @param surname     surname of the customer
     * @param tickets     places with ticket types to be reserved
     * @return Mono of ReservationResource
     */
    public Mono<ReservationResource> reserve(String screeningId, String name, String surname, Set<Ticket> tickets) {

        //check if screening exists
        final Mono<Screening> screeningMono = this.screeningRepository.findById(screeningId)
                .switchIfEmpty(Mono.error(new IllegalStateException(String.format("Screening with id: '%s' does not exists", screeningId))));

        // zipping Reservation and Screening to do operations on both in same methods.
        // E.g. to #1 execute domain functions on both and #2 save both domains in one method not to rely on @Transactional
        // (not to book seats in Screening and save it in one method and create Reservation and save in other
        return
                screeningMono
                        .zipWhen(screening -> Mono.just(new Reservation(screeningId, screening.getStartTime(), name, surname, tickets)))

                        //book seats, calculate total price, calculate expiration date
                        .flatMap(tuple -> {
                            tuple.getT1().bookSeats(tickets.stream().map(Ticket::getSeat).collect(Collectors.toList()));
                            tuple.getT2().calculateTotalPrice();
                            tuple.getT2().calculateExpirationDate();
                            return Mono.just(tuple);
                        })

                        //save screening and reservation
                        .flatMap(tuple -> this.screeningRepository.save(tuple.getT1())
                                .flatMap(_x -> this.reservationRepository.save(tuple.getT2())))

                        //schedule reservation cancel.
                        //exception from #cancel automatically swallowed. in this context it can only be thrown if reservation was already confirmed/cancelled
                        .doOnNext(reservation -> Mono
                                //.delay(Duration.ofSeconds(5)) //for testing
                                .delay(Duration.between(LocalDateTime.now(), reservation.getExpiresAt()))
                                .doOnNext(_x -> this.cancel(reservation.getId()).subscribe())
                                .subscribe()
                        )
                        .flatMap(reservation ->
                                Mono.just(
                                        new ReservationResource(reservation.getId(), reservation.getToken(), reservation.getStatus(), reservation.getExpiresAt(), reservation.getScreeningId(),
                                                reservation.getScreeningStartTime(), reservation.getName(), reservation.getSurname(), reservation.getFormattedPrice(), reservation.getTickets()))
                        );
    }

    /**
     * @param reservationId identifier of reservation to be confirmed
     * @param token         hash to identify reservation
     * @return Mono of ReservationResource
     */
    public Mono<ReservationResource> confirm(String reservationId, String token) {
        //check if reservation exists
        final Mono<Reservation> reservation = this.reservationRepository.findByIdAndStatusAndToken(reservationId, ReservationStatus.OPEN, token)
                .switchIfEmpty(Mono.error(new IllegalStateException(String.format("Open reservation with id: '%s' does not exists", reservationId))));

        //confirm reservation
        return reservation
                .flatMap(domain -> {
                    domain.confirm();
                    return Mono.just(domain);
                })
                .flatMap(domain -> {
                    this.reservationRepository.save(domain);
                    return Mono.just(
                            new ReservationResource(domain.getId(), domain.getToken(), domain.getStatus(), domain.getExpiresAt(), domain.getScreeningId(),
                                    domain.getScreeningStartTime(), domain.getName(), domain.getSurname(), domain.getFormattedPrice(), domain.getTickets()));
                });
    }

    /**
     * @param reservationId identifier of reservation to be cancelled
     * @return CompletableFuture of Reservation domain object
     */
    public Mono<Reservation> cancel(String reservationId) {
        //check if open reservation exists
        final Mono<Reservation> reservation = this.reservationRepository.findByIdAndStatus(reservationId, ReservationStatus.OPEN)
                .switchIfEmpty(Mono.error(new IllegalStateException(String.format("Reservation with id: '%s' does not exists", reservationId))));

        //check if screening exists
        final Mono<Screening> screening = reservation.flatMap(r -> this.screeningRepository.findById(r.getScreeningId()))
                .switchIfEmpty(Mono.error(new IllegalStateException("Screeninig does not exists")));

        return reservation
                .zipWith(screening)
                //cancel reservation and free screening places
                .flatMap(tuple -> {
                    tuple.getT1().cancel();
                    tuple.getT2().freeReservedSeats(tuple.getT1().getReservedPlaces());
                    return Mono.just(tuple);
                })
                .flatMap(tuple ->
                        screeningRepository.save(tuple.getT2())
                                .flatMap(_x -> this.reservationRepository.save(tuple.getT1())));
    }

}
