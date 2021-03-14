package com.jarrvis.ticketbooking.domain;

import com.jarrvis.ticketbooking.domain.exception.SeatAlreadyBookedException;
import com.jarrvis.ticketbooking.domain.exception.SeatDoesNotExistException;
import com.jarrvis.ticketbooking.domain.exception.SeatNotReservedException;
import com.jarrvis.ticketbooking.domain.exception.SingleSeatLeftAfterBookingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@AllArgsConstructor
public class Screening {

    @Getter
    private String id;

    @Getter
    private final LocalDateTime startTime;

    @Getter
    private final LocalDateTime endTime;

    @Getter
    private final String movie;

    @Getter
    private final String room;

    @Getter
    private final Integer rows;

    @Getter
    private final Integer seatsPerRow;

    private Set<ScreeningSeat> seats;


    public Set<ScreeningSeat> getSeats() {
        if (this.seats == null) {
            this.seats = new HashSet<>();
            IntStream.range(1, this.rows + 1).boxed()
                    .forEach(row -> IntStream.range(1, this.seatsPerRow + 1).boxed()
                            .forEach(seat -> this.seats.add(ScreeningSeat.of(Seat.of(row, seat), SeatStatus.FREE))));
        }
        return this.seats;
    }

    /**
     * @param row   number of row
     * @param place number of place in a row
     * @return true if place is free, false otherwise
     */
    public boolean isSeatFree(int row, int place) {
        return SeatStatus.FREE == getSeatStatus(row, place);
    }


    /**
     * @param row   number of row
     * @param place number of place in a row
     * @return true if place is reserved, false otherwise
     */
    public boolean isSeatReserved(int row, int place) {
        return SeatStatus.RESERVED == getSeatStatus(row, place);

    }

    /**
     * @return Representation of free seats per screening as map:
     * key: row number
     * value: list of free places numbers
     */
    public List<Seat> availableSeats() {
        return this.getSeats().stream()
                .filter(seat -> seat.getSeatStatus() == SeatStatus.FREE)
                .map(ScreeningSeat::getSeat)
                .collect(Collectors.toList());
    }

    public void bookSeat(Seat seat) {
        if (!this.isSeatFree(seat.getRowNumber(), seat.getSeatNumber())) {
            throw new SeatAlreadyBookedException(seat);
        }
        if (this.isSingleSeatLeftAfterBooking(seat.getRowNumber(), seat.getSeatNumber())) {
            throw new SingleSeatLeftAfterBookingException(seat);
        }
        this.getSeats().remove(ScreeningSeat.of(seat));
        this.getSeats().add(ScreeningSeat.of(seat, SeatStatus.RESERVED));
    }

    /**
     * @param freeSeats collection of seats to be booked
     */
    public void bookSeats(Collection<Seat> freeSeats) {
        freeSeats.forEach(this::bookSeat);
    }


    public void freeReservedSeat(Seat seat) {
        if (!this.isSeatReserved(seat.getRowNumber(), seat.getSeatNumber())) {
            throw new SeatNotReservedException(seat);
        }
        this.getSeats().remove(ScreeningSeat.of(seat));
        this.getSeats().add(ScreeningSeat.of(seat, SeatStatus.FREE));
    }

    /**
     * @param reservedSeats list of reserved seats to be freed
     */
    public void freeReservedSeats(List<Seat> reservedSeats) {
        reservedSeats.forEach(this::freeReservedSeat);
    }

    public SeatStatus getSeatStatus(int row, int place) {
        Optional<ScreeningSeat> status = this.getSeats().stream()
                .filter(x -> x.equals(ScreeningSeat.of(Seat.of(row, place))))
                .findFirst();
        if (!status.isPresent()) {
            throw new SeatDoesNotExistException(row, place);
        }
        return status.get().getSeatStatus();
    }

    /**
     * @return true if single seat would be left between two 'reserved' seats after booking
     * <p>
     * Only 'between two reserved seats' situation is handled as in requirements. No side seats left handling
     */
    private boolean isSingleSeatLeftAfterBooking(int row, int place) {
        if (place + 2 <= this.seatsPerRow) {
            if (isSeatFree(row, place + 1) && !isSeatFree(row, place + 2)) {
                return true;
            }
        }
        if (place - 2 > 0) {
            return isSeatFree(row, place - 1) && !isSeatFree(row, place - 2);
        }
        return false;
    }

}
