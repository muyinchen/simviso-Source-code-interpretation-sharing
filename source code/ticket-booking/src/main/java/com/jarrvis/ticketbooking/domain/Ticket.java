package com.jarrvis.ticketbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
public class Ticket {

    private Seat seat;

    @EqualsAndHashCode.Exclude
    TicketType ticketType;

    public BigDecimal getPriceValue() {
        return this.ticketType.getValue();
    }
}
