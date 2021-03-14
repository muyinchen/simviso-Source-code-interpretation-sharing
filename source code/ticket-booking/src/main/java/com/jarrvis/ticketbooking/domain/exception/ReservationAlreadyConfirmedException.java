package com.jarrvis.ticketbooking.domain.exception;


public class ReservationAlreadyConfirmedException extends RuntimeException {

    private static final long serialVersionUID = 5991191955244366152L;

    private static final String message = "Reservation already confirmed";


    public ReservationAlreadyConfirmedException() {
        super(message);
    }

}
