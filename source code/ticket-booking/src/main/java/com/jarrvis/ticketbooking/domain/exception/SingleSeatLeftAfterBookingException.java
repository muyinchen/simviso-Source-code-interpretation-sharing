package com.jarrvis.ticketbooking.domain.exception;


import com.jarrvis.ticketbooking.domain.Seat;

public class SingleSeatLeftAfterBookingException extends RuntimeException {

    private static final long serialVersionUID = 5991191955244366155L;

    private static final String messageTemplate = "Seat with row number: %s and seat number: %s cannot be reserved since it would leave free seat between two already reserved seats";


    public SingleSeatLeftAfterBookingException(Seat seat) {
        super(String.format(messageTemplate, seat.getRowNumber(), seat.getSeatNumber()));
    }

    public SingleSeatLeftAfterBookingException(int rowNumber, int seatNumber) {
        super(String.format(messageTemplate, rowNumber, seatNumber));
    }

}
