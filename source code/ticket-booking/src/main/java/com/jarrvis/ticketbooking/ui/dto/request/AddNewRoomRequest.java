package com.jarrvis.ticketbooking.ui.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddNewRoomRequest {

    public static final int MAX_NUMBER_OF_ROWS = 30;
    public static final int MAX_NUMBER_OF_SEATS_PER_ROW = 30;

    @NotEmpty
    @ApiModelProperty(value = "Multiplex room name - unique in multiplex.", required = true)
    private String name;

    @Range(min = 1, max = MAX_NUMBER_OF_ROWS)
    @ApiModelProperty(value = "Number of rows in multiplex room.", required = true)
    private Integer rows;

    @Range(min = 1, max = MAX_NUMBER_OF_ROWS)
    @ApiModelProperty(value = "Number of seats in row in multiplex room.", required = true)
    private Integer seatsPerRow;
}
