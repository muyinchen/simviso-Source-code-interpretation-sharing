package com.jarrvis.ticketbooking.infrastructure.mongo.screening;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public interface ScreeningMongoRepository extends ReactiveMongoRepository<ScreeningDocument, String> {

    Flux<ScreeningDocument> findByStartTimeBetweenOrderByMovieAscStartTimeAsc(LocalDateTime startDate, LocalDateTime endDate);
    Mono<Boolean> existsByRoomAndStartTimeBeforeAndEndTimeAfter(String room, LocalDateTime startTime, LocalDateTime endTime);

}
