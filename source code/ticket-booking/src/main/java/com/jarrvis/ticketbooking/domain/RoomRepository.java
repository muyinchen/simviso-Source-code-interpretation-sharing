package com.jarrvis.ticketbooking.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomRepository {

    Mono<Room> findByName(String name);
    Mono<Room> save(Room movie);
    Flux<Room> findAll();
    Mono<Boolean> existsByName(String name);
}
