package com.jarrvis.ticketbooking.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResource {

    private String name;
    private String description;
    private Long duration;
}
