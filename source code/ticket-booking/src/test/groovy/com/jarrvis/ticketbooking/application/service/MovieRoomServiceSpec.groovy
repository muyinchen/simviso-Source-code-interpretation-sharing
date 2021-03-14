package com.jarrvis.ticketbooking.application.service

import com.jarrvis.ticketbooking.domain.Room
import com.jarrvis.ticketbooking.domain.RoomRepository
import com.jarrvis.ticketbooking.ui.exception.AlreadyExistException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

class MovieRoomServiceSpec extends Specification {

    private MovieRoomService movieRoomService
    private RoomRepository roomRepository

    def setup() {
        roomRepository = Mock(RoomRepository)
        movieRoomService = new MovieRoomService(roomRepository)
    }

    def "Should list all rooms"() {
        given:
            def room = new Room("Dream", 10, 15)
            roomRepository.findAll() >> Flux.just(room)
        when:
            def resource = movieRoomService.listAllRooms().blockFirst()
        then:
            resource.name == room.name
            resource.rows == room.rows
            resource.seatsPerRow == room.seatsPerRow
    }

    def "Should not be able to add room with name that already exist"() {
        given:
            roomRepository.existsByName(_ as String) >> Mono.just(true)

        when:
            movieRoomService.addNewRoom("Dream", 10, 15)
                .block()
        then:
            thrown(AlreadyExistException)

    }

    def "Should be able to add room"() {
        given:
            roomRepository.existsByName(_ as String) >> Mono.just(false)
            roomRepository.save(_ as Room) >> { Room room -> Mono.just(room) }

        when:
            def result = movieRoomService.addNewRoom("Dream", 10, 15)
                .block()
        then:
            result
    }
}