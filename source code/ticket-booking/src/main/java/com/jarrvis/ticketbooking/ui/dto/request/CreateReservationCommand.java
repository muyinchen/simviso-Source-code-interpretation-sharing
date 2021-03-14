package com.jarrvis.ticketbooking.ui.dto.request;

import com.jarrvis.ticketbooking.domain.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationCommand {

    @NotEmpty
    private String screeningId;

    @NotEmpty
    @Pattern(regexp = "([\\p{Lu}][\\p{Ll}]*)")
    private String name;

    @NotEmpty
    @Pattern(regexp = "^([\\p{Lu}][\\p{Ll}]*(-[\\p{Lu}][\\p{Ll}]*)?)")
    private String surname;

    @NotNull
    private Set<Ticket> tickets;

}
