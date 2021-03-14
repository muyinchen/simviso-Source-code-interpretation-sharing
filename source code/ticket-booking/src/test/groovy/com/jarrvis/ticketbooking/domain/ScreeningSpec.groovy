package com.jarrvis.ticketbooking.domain

import com.jarrvis.ticketbooking.domain.exception.SeatAlreadyBookedException
import com.jarrvis.ticketbooking.domain.exception.SeatDoesNotExistException
import com.jarrvis.ticketbooking.domain.exception.SingleSeatLeftAfterBookingException
import spock.lang.Specification

import java.time.LocalDateTime

class ScreeningSpec extends Specification {

    def "Can book free seat"() {
        given:
            def screening = new Screening(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Joker", "Dream", 10, 15)
        when:
            screening.bookSeat(Seat.of(1, 1))
        then:
            !screening.isSeatFree(1, 1)
    }

    def "Can only books seats that exists"() {
        given:
            def screening = new Screening(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Joker", "Dream", 10, 15)
        when:
            screening.bookSeat(Seat.of(11, 20))
        then:
            thrown(SeatDoesNotExistException)
    }

    def "Can not book seat that is already reserved"() {
        given:
            def screening = new Screening(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Joker", "Dream", 10, 15)
        when:
            screening.bookSeat(Seat.of(1, 1))
        and: 'try to book that seat again'
            screening.bookSeat(Seat.of(1, 1))
        then:
            thrown(SeatAlreadyBookedException)
    }

    def "Can not book seat leaving one seat free between reserved seats"() {
        given:
            def screening = new Screening(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Joker", "Dream", 10, 15)
        when: 'booking first seat'
            screening.bookSeat(Seat.of(1, 1))
        and: 'try to book seat again leaving free seat between two reserved'
            screening.bookSeat(Seat.of(1, 3))
        then:
            thrown(SingleSeatLeftAfterBookingException)
    }

    def "Available seats test"() {
        given:
            def screening = new Screening(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Joker", "Dream", 10, 15)
        when: "Booking a few seats"
            screening.bookSeat(Seat.of(1, 1))
            screening.bookSeat(Seat.of(1, 2))
        then:
            screening.getSeatStatus(1, 1) is SeatStatus.RESERVED
            screening.getSeatStatus(1, 2) is SeatStatus.RESERVED
        and:
            def availableSeats = screening.availableSeats()
            !availableSeats.contains(Seat.of(1, 1))
            !availableSeats.contains(Seat.of(1, 2))
    }

}