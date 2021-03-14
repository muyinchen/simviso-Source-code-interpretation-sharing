package com.jarrvis.ticketbooking.domain.exception;


import com.jarrvis.ticketbooking.domain.Seat;

public class SeatAlreadyBookedException extends RuntimeException {

    private static final long serialVersionUID = 5991191955244366152L;

    private static final String messageTemplate = "Seat with row number: %s and seat number: %s is already booked";


    public SeatAlreadyBookedException(Seat seat) {
        super(String.format(messageTemplate, seat.getRowNumber(), seat.getSeatNumber()));
    }

    public SeatAlreadyBookedException(int rowNumber, int seatNumber) {
        super(String.format(messageTemplate, rowNumber, seatNumber));
    }
}
