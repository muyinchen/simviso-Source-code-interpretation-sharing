package com.jarrvis.ticketbooking.application.service

import com.jarrvis.ticketbooking.domain.*
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDateTime

class ScreeningServiceSpec extends Specification {

    private ScreeningService screeningService
    private ScreeningRepository screeningRepository
    private MovieRepository movieRepository
    private RoomRepository roomRepository

    def setup() {
        movieRepository = Mock(MovieRepository)
        screeningRepository = Mock(ScreeningRepository)
        roomRepository = Mock(RoomRepository)

        screeningService = new ScreeningService(screeningRepository, roomRepository, movieRepository)
    }

    def "Should not be possible to add screening for non existing movie"() {
        given:
            movieRepository.findByName(_ as String) >> Mono.empty()
            roomRepository.findByName(_ as String) >> Mono.just(new Room("Dream", 10, 15))

        when:
            screeningService.addNewScreening(LocalDateTime.now().plusDays(1), "Joker", "Dream")
                    .block()
        then:
            thrown(IllegalStateException)
    }

    def "Should not be possible to add screening for non existing room"() {
        given:
            roomRepository.findByName(_ as String) >> Mono.empty()
            movieRepository.findByName(_ as String) >> Mono.just(new Movie("Joker", "Joker", 120))

        when:
            screeningService.addNewScreening(LocalDateTime.now().plusDays(1), "Joker", "Dream")
                    .block()
        then:
            thrown(IllegalStateException)
    }

    def "Should save screening"() {
        given:
            movieRepository.findByName(_ as String) >> Mono.just(new Movie("Joker", "Joker",  120))
            roomRepository.findByName(_ as String) >> Mono.just(new Room("Dream", 10, 15))
            screeningRepository.existsByRoomAndStartTimeBeforeAndEndTimeAfter(_ as String, _ as LocalDateTime, _ as LocalDateTime) >> Mono.just(false)
            screeningRepository.save(_ as Screening) >> { Screening screening -> Mono.just(screening)}

        when:
            def result = screeningService.addNewScreening(LocalDateTime.now().plusDays(1), "Joker", "Dream")
                    .block()
        then:
            result
            //1 * screeningRepository.save(!null)
    }

}