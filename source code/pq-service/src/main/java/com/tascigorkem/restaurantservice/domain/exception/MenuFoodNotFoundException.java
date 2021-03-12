package com.tascigorkem.restaurantservice.domain.exception;

public class MenuFoodNotFoundException extends ResourceNotFoundException {

    public MenuFoodNotFoundException(String keyName, String keyValue) {
        super("MenuFood", keyName, keyValue);
    }
}
