package com.jarrvis.ticketbooking.application.service;

import com.jarrvis.ticketbooking.domain.Movie;
import com.jarrvis.ticketbooking.domain.MovieRepository;
import com.jarrvis.ticketbooking.domain.Room;
import com.jarrvis.ticketbooking.domain.RoomRepository;
import com.jarrvis.ticketbooking.domain.Screening;
import com.jarrvis.ticketbooking.domain.ScreeningRepository;
import com.jarrvis.ticketbooking.ui.dto.response.ScreeningResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;

    public ScreeningService(
            final ScreeningRepository screeningRepository,
            final RoomRepository roomRepository,
            final MovieRepository movieRepository
    ) {
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
    }

    /**
     *
     * @param startTime the time from which search of screenings should happen
     * @param endTme the time to which search of screenings should happen
     * @return Flux of ScreeningResource
     */
    public Flux<ScreeningResource> searchForScreenings(LocalDateTime startTime, LocalDateTime endTme) {
        return this.screeningRepository.findByStartTimeBetweenOrderByMovieAscStartTimeAsc(startTime, endTme)
                .flatMap(screening -> Mono.just(
                        new ScreeningResource(screening.getId(), screening.getMovie(), screening.getRoom(), screening.getStartTime(), screening.getEndTime(), screening.getRows(), screening.getSeatsPerRow(), screening.availableSeats())));
    }

    /**
     *
     * @param startTime the screening start time
     * @param movieName the name of the movie
     * @param roomName the name of the screening room
     * @return Mono of ScreeningResource
     */
    public Mono<ScreeningResource> addNewScreening(LocalDateTime startTime, String movieName, String roomName) {
        // check if room exists
        final Mono<Room> room = this.roomRepository.findByName(roomName)
                .switchIfEmpty(Mono.error(new IllegalStateException(String.format("Room with name: '%s' does not exists", roomName))));

        // check if movie exists
        final Mono<Movie> movie = this.movieRepository.findByName(movieName)
                .switchIfEmpty(Mono.error(new IllegalStateException(String.format("Movie with name: '%s' does not exists", movieName))));

        // create screening candidate
        final Mono<Screening> screeningMono = Mono.zip(room, movie)
                .flatMap(tuple ->
                        Mono.just(new Screening(
                                startTime,
                                startTime.plusMinutes(tuple.getT2().getDuration()),
                                movieName,
                                roomName,
                                tuple.getT1().getRows(),
                                tuple.getT1().getSeatsPerRow()))
                );


        return screeningMono
                // check if screening candidate overlaps with already existing screenings (same room, overlapping hours)
                .flatMap(screening -> this.screeningRepository.existsByRoomAndStartTimeBeforeAndEndTimeAfter(roomName, screening.getEndTime(), screening.getStartTime())
                        .flatMap(exists -> {
                            if (exists) {
                                throw new IllegalStateException("Overlapping screening already exists");
                            }
                            return this.screeningRepository.save(screening)
                                    .flatMap(screeningDomain -> Mono.just(
                                            new ScreeningResource(screeningDomain.getId(), screeningDomain.getMovie(), screeningDomain.getRoom(), screeningDomain.getStartTime(), screeningDomain.getEndTime(), screeningDomain.getRows(), screeningDomain.getSeatsPerRow(), screeningDomain.availableSeats())));

                        }));
    }

}
