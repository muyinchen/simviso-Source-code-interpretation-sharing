package com.jarrvis.ticketbooking.domain;

import lombok.Value;

@Value
public class Room {

    private String name;
    private int rows;
    private int seatsPerRow;
}
