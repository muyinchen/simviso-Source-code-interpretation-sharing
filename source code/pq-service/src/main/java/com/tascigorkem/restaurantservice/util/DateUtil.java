package com.tascigorkem.restaurantservice.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    private static DateUtil instance;

    public static DateUtil getInstance() {
        if (instance == null) {
            synchronized (DateUtil.class) {
                if (instance == null) {
                    instance = new DateUtil();
                }
            }
        }
        return instance;
    }

    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }
}