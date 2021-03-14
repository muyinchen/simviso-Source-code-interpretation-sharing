package com.jarrvis.ticketbooking.domain.exception;


import com.jarrvis.ticketbooking.domain.Seat;

public class SeatDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 5991191955244366153L;

    private static final String messageTemplate = "Seat with row number: %s and seat number: %s does not exist";


    public SeatDoesNotExistException(Seat seat) {
        super(String.format(messageTemplate, seat.getRowNumber(), seat.getSeatNumber()));
    }

    public SeatDoesNotExistException(int rowNumber, int seatNumber) {
        super(String.format(messageTemplate, rowNumber, seatNumber));
    }

}
