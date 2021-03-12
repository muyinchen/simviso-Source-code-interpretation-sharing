package com.tascigorkem.restaurantservice.domain.exception;

public class MenuNotFoundException extends ResourceNotFoundException {

    public MenuNotFoundException(String keyName, String keyValue) {
        super("Menu", keyName, keyValue);
    }
}
