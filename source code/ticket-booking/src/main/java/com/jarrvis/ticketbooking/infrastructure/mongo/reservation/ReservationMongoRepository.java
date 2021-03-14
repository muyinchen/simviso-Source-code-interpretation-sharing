package com.jarrvis.ticketbooking.infrastructure.mongo.reservation;

import com.jarrvis.ticketbooking.domain.ReservationStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReservationMongoRepository extends ReactiveMongoRepository<ReservationDocument, String> {

    Mono<ReservationDocument> findByIdAndStatusAndToken(String id, ReservationStatus status, String token);
    Mono<ReservationDocument> findByIdAndStatus(String id, ReservationStatus status);
}
