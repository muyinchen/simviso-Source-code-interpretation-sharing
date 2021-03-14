package com.jarrvis.ticketbooking.domain;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Movie {

    private String name;

    private String description;

    private Long duration;
}
