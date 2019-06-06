package com.dockerx.webflux.usermanage.domain;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/6 23:15.
 */
public enum Role {

    CUSTOMER(1), AUTHOR(2), ADMIN(3);

    private int value;

    Role(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }

}
