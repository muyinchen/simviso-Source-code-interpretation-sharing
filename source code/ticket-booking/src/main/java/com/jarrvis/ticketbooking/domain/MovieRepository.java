package com.jarrvis.ticketbooking.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieRepository {

    Mono<Movie> findByName(String name);
    Mono<Movie> save(Movie movie);
    Flux<Movie> findAll();
    Mono<Boolean> existsByName(String name);
}
