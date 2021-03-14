package com.jarrvis.ticketbooking.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;

@Value
class Price {

    BigDecimal value;
    Currency currency;
}
