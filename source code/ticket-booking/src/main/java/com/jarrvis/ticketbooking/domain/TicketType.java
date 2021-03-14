package com.jarrvis.ticketbooking.domain;

import java.math.BigDecimal;

public enum TicketType {
    ADULT(BigDecimal.valueOf(25)),
    STUDENT(BigDecimal.valueOf(18)),
    CHILD(BigDecimal.valueOf(12.5));

    private BigDecimal value;

    TicketType(BigDecimal valueOf) {
        value = valueOf;
    }

    public BigDecimal getValue() {
        return value;
    }
}
