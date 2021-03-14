package com.jarrvis.ticketbooking.infrastructure.mongo.screening;

import com.jarrvis.ticketbooking.domain.Screening;
import com.jarrvis.ticketbooking.domain.ScreeningRepository;
import com.jarrvis.ticketbooking.domain.ScreeningSeat;
import com.jarrvis.ticketbooking.domain.SeatStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class ScreeningMongoAdapter implements ScreeningRepository {

    private final ScreeningMongoRepository screeningMongoRepository;

    public ScreeningMongoAdapter(ScreeningMongoRepository screeningMongoRepository) {
        this.screeningMongoRepository = screeningMongoRepository;
    }


    @Override
    public Mono<Screening> findById(String id) {
        return this.screeningMongoRepository.findById(id)
                .flatMap(screeningDocument -> Mono.just(screeningDocument.mutateTo()));
    }

    @Override
    public Mono<Screening> save(Screening screening) {
        ScreeningDocument document = new ScreeningDocument(screening.getId(), screening.getStartTime(), screening.getEndTime(),
                screening.getMovie(), screening.getRoom(), screening.getRows(), screening.getSeatsPerRow(), screening.getSeats());
        return this.screeningMongoRepository.save(document)
                .flatMap(entity -> Mono.just(entity.mutateTo()));
    }

    @Override
    public Flux<Screening> findByStartTimeBetweenOrderByMovieAscStartTimeAsc(LocalDateTime startDate, LocalDateTime endDate) {
        return this.screeningMongoRepository.findByStartTimeBetweenOrderByMovieAscStartTimeAsc(startDate, endDate)
                .flatMap(screeningDocument -> Mono.just(screeningDocument.mutateTo()));
    }

    @Override
    public Mono<Boolean> existsByRoomAndStartTimeBeforeAndEndTimeAfter(String room, LocalDateTime startTime, LocalDateTime endTime) {
        return this.screeningMongoRepository.existsByRoomAndStartTimeBeforeAndEndTimeAfter(room, startTime, endTime);
    }

}
