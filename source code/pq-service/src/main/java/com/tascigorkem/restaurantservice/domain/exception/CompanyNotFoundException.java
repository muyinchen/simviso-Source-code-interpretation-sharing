package com.tascigorkem.restaurantservice.domain.exception;

public class CompanyNotFoundException extends ResourceNotFoundException {

    public CompanyNotFoundException(String keyName, String keyValue) {
        super("Company", keyName, keyValue);
    }
}
