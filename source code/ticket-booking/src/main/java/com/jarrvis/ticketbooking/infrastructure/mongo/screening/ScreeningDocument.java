package com.jarrvis.ticketbooking.infrastructure.mongo.screening;

import com.jarrvis.ticketbooking.domain.Screening;
import com.jarrvis.ticketbooking.domain.ScreeningSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "screenings")
public class ScreeningDocument {

    @Id
    private String id;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotEmpty
    private String movie;

    @NotEmpty
    private String room;

    @NotNull
    @Range(min = 1, max = 30)
    private Integer rows;

    @NotNull
    @Range(min = 1, max = 30)
    private Integer seatsPerRow;

    @NotNull
    private Set<ScreeningSeat> seats;

    public Screening mutateTo() {
        return new Screening(id, startTime, endTime, movie, room, rows, seatsPerRow, seats);
    }


}
