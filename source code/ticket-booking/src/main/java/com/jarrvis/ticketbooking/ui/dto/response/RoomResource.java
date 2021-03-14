package com.jarrvis.ticketbooking.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResource {

    private String name;
    private int rows;
    private int seatsPerRow;

}
