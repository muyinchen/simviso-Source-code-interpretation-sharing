package com.tascigorkem.restaurantservice.domain.exception;

public class RestaurantNotFoundException extends ResourceNotFoundException {

    public RestaurantNotFoundException(String keyName, String keyValue) {
        super("Restaurant", keyName, keyValue);
    }
}
