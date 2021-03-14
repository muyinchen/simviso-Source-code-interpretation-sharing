package com.jarrvis.ticketbooking.infrastructure.mongo.room;

import com.jarrvis.ticketbooking.domain.Room;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@Document(collection = "rooms")
public class RoomDocument {

    @Id
    public String id;

    @NotEmpty
    @Indexed(unique=true)
    private final String name;

    @Range(min = 1, max = 30)
    private final int rows;

    @Range(min = 1, max = 30)
    private final int seatsPerRow;

    public Room mutateTo() {
        return new Room(name, rows, seatsPerRow);
    }
}
