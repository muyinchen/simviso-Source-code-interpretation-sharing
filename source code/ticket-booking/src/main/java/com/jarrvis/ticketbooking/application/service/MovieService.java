package com.jarrvis.ticketbooking.application.service;

import com.jarrvis.ticketbooking.domain.Movie;
import com.jarrvis.ticketbooking.domain.MovieRepository;
import com.jarrvis.ticketbooking.ui.dto.response.MovieResource;
import com.jarrvis.ticketbooking.ui.exception.AlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(
            final MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
    }

    /**
     *
     * @param name the name of the movie
     * @param description the description of the movie
     * @param duration the duration of the movie in minutes
     * @return Mono of true/false
     */
    public Mono<Boolean> addNewMovie(String name, String description, Long duration) {
        return this.movieRepository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        throw new AlreadyExistException(String.format("Movie with name: %s already exists", name));
                    }
                    return this.movieRepository.save(new Movie(name, description, duration))
                            .flatMap(movie -> Mono.just(true));
                });
    }

    /**
     *
     * @return Flux of MovieResource
     */
    public Flux<MovieResource> listAllMovies() {
        return this.movieRepository.findAll()
                .flatMap(domain -> Mono.just(new MovieResource(domain.getName(), domain.getDescription(), domain.getDuration())));
    }
}
