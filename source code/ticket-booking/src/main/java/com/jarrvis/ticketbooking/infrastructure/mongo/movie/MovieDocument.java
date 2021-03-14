package com.jarrvis.ticketbooking.infrastructure.mongo.movie;

import com.jarrvis.ticketbooking.domain.Movie;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Document(collection = "movies")
public class MovieDocument {

    @Id
    public String id;

    @NotEmpty
    @Indexed(unique=true)
    private final String name;

    @NotEmpty
    private final String description;

    @NotNull
    @Range(min=5, max = 240)
    private final Long duration;

    public Movie mutateTo() {
        return new Movie(name, description, duration);
    }
}
