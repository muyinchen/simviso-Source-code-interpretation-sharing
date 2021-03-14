package com.jarrvis.ticketbooking.ui.dto.response;

import com.jarrvis.ticketbooking.domain.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningResource {

    private String id;
    private String movie;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int rows;
    private int seatsPerRow;
    private List<Seat> freePlaces;
}
