package com.jarrvis.ticketbooking.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor(staticName = "of")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class ScreeningSeat {

    @NonNull
    private Seat seat;

    @EqualsAndHashCode.Exclude
    private SeatStatus seatStatus;

}
