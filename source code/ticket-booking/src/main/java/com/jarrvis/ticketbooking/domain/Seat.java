package com.jarrvis.ticketbooking.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Seat {

    private Integer rowNumber;
    private Integer seatNumber;
}
