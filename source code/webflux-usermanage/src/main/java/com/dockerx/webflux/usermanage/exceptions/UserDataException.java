package com.dockerx.webflux.usermanage.exceptions;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/9 1:54.
 */
public class UserDataException extends RuntimeException {
    public UserDataException(String message) {
        super(message);
    }
}
