package com.jarrvis.ticketbooking.domain;

import reactor.core.publisher.Mono;

public interface ReservationRepository {

    Mono<Reservation> findByIdAndStatusAndToken(String id, ReservationStatus status, String token);
    Mono<Reservation> save(Reservation reservation);
    Mono<Reservation> findByIdAndStatus(String id, ReservationStatus status);
}
