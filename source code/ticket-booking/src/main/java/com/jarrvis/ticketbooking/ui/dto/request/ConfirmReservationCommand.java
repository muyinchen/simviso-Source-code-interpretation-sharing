package com.jarrvis.ticketbooking.ui.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmReservationCommand {

    @ApiModelProperty(value = "Reservation identifier to be confirmed.", required = true)
    @NotEmpty
    private String reservationId;

    @ApiModelProperty(value = "Confirm reservation token.", required = true)
    @NotEmpty
    private String token;


}
