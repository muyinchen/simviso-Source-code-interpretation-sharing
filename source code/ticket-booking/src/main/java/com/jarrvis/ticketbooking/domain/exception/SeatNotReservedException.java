package com.jarrvis.ticketbooking.domain.exception;


import com.jarrvis.ticketbooking.domain.Seat;

public class SeatNotReservedException extends RuntimeException {

    private static final long serialVersionUID = 5991191955244366154L;

    private static final String messageTemplate = "Place with row number: %s and seat number: %s is not reserved";


    public SeatNotReservedException(Seat seat) {
        super(String.format(messageTemplate, seat.getRowNumber(), seat.getSeatNumber()));
    }

    public SeatNotReservedException(int rowNumber, int seatNumber) {
        super(String.format(messageTemplate, rowNumber, seatNumber));
    }
}
