package com.dockerx.webflux.usermanage.exceptions;

import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/9 0:17.
 */
public class CustomValidationException extends RuntimeException {
    private List<ObjectError> errors;

    public CustomValidationException(List<ObjectError> errors) {
        super("Validation errors");
        this.errors = errors;
    }



    public List<ObjectError> getErrors() {
        return errors;
    }
}
