package com.jarrvis.ticketbooking.infrastructure.mongo.reservation;

import com.jarrvis.ticketbooking.domain.Currency;
import com.jarrvis.ticketbooking.domain.Reservation;
import com.jarrvis.ticketbooking.domain.ReservationStatus;
import com.jarrvis.ticketbooking.domain.ScreeningSeat;
import com.jarrvis.ticketbooking.domain.Ticket;
import com.jarrvis.ticketbooking.domain.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reservations")
public class ReservationDocument {

    @Id
    private String id;

    private String token;

    private ReservationStatus status;

    private LocalDateTime createdAt;

    @NotEmpty
    private String screeningId;

    @NotNull
    private LocalDateTime screeningStartTime;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @NotNull
    private Set<Ticket> tickets;

    @NotNull
    private LocalDateTime expiresAt;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private Currency currency;

    public Reservation mutateTo() {
        return new Reservation(id, token, status, expiresAt, createdAt, screeningId, screeningStartTime, name, surname, tickets, totalPrice, currency);
    }

}
