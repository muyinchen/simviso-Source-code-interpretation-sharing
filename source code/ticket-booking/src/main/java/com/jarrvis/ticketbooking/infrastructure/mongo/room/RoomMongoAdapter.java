package com.jarrvis.ticketbooking.infrastructure.mongo.room;

import com.jarrvis.ticketbooking.domain.Room;
import com.jarrvis.ticketbooking.domain.RoomRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RoomMongoAdapter implements RoomRepository {

    private final RoomMongoRepository roomMongoRepository;

    public RoomMongoAdapter(RoomMongoRepository roomMongoRepository) {
        this.roomMongoRepository = roomMongoRepository;
    }

    @Override
    public Flux<Room> findAll() {
        return this.roomMongoRepository.findAll()
                .flatMap(roomDocument ->
                        Mono.just(new Room(roomDocument.getName(), roomDocument.getRows(), roomDocument.getSeatsPerRow())));
    }

    @Override
    public Mono<Room> findByName(String name) {
        return this.roomMongoRepository.findByName(name)
                .flatMap(roomDocument ->
                        Mono.just(new Room(roomDocument.getName(), roomDocument.getRows(), roomDocument.getSeatsPerRow())));
    }

    @Override
    public Mono<Room> save(Room room) {
        RoomDocument document = new RoomDocument(room.getName(), room.getRows(), room.getSeatsPerRow());
        return this.roomMongoRepository.save(document)
                .flatMap(entity -> Mono.just(entity.mutateTo()));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return this.roomMongoRepository.existsByName(name);
    }
}
