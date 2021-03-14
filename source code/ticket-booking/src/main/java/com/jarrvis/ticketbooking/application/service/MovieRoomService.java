package com.jarrvis.ticketbooking.application.service;

import com.jarrvis.ticketbooking.domain.Room;
import com.jarrvis.ticketbooking.domain.RoomRepository;
import com.jarrvis.ticketbooking.ui.dto.response.RoomResource;
import com.jarrvis.ticketbooking.ui.exception.AlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Transactional
public class MovieRoomService {

    private final RoomRepository roomRepository;

    public MovieRoomService(
            final RoomRepository roomRepository
    ) {
        this.roomRepository = roomRepository;
    }


    /**
     *
     * @param name name of the screening room to be added
     * @param rows number of rows in the screening room to be added
     * @param seatsPerRow number of seats in one row in screening room to be added
     * @return Mono of true/false
     */
    public Mono<Boolean> addNewRoom(String name, int rows, int seatsPerRow) {
        return this.roomRepository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        throw new AlreadyExistException(String.format("Room with name: %s already exists", name));
                    }
                    return this.roomRepository.save(new Room(name, rows, seatsPerRow))
                            .flatMap(room -> Mono.just(true));
                });
    }

    /**
     *
     * @return Flux of RoomResource
     */
    public Flux<RoomResource> listAllRooms() {
        return this.roomRepository.findAll()
                .flatMap(domain -> Mono.just(new RoomResource(domain.getName(), domain.getRows(), domain.getSeatsPerRow())));

    }

}
