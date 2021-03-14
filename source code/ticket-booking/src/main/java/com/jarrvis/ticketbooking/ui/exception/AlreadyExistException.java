package com.jarrvis.ticketbooking.ui.exception;


public class AlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 5991191955244366152L;

    /**
     * Constructor accepting an error message.
     *
     * @param message
     *            message
     */
    public AlreadyExistException(String message) {
        super(message);
    }

}
