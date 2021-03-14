package com.jarrvis.ticketbooking.infrastructure.mongo.movie;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieMongoRepository extends ReactiveMongoRepository<MovieDocument, String> {

    Mono<MovieDocument> findByName(String name);
    Flux<MovieDocument> findAll();
    Mono<Boolean> existsByName(String name);

}
