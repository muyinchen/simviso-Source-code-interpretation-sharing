package com.tascigorkem.restaurantservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

    ResourceNotFoundException(String resourceType, String keyName, String keyValue) {
        super(resourceType + " with " + keyName + "[" + keyValue + "] not found.");
    }

}
