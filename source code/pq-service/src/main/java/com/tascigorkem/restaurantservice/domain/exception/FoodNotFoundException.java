package com.tascigorkem.restaurantservice.domain.exception;

public class FoodNotFoundException extends ResourceNotFoundException {

    public FoodNotFoundException(String keyName, String keyValue) {
        super("Food", keyName, keyValue);
    }
}
