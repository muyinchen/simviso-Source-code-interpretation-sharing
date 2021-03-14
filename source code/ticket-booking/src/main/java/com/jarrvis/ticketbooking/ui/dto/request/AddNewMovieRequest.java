package com.jarrvis.ticketbooking.ui.dto.request;

import com.jarrvis.ticketbooking.ui.validation.DateRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddNewMovieRequest {

    @NotEmpty
    @ApiModelProperty(value = "Movie name.", required = true)
    private String name;

    @NotEmpty
    @ApiModelProperty(value = "Movie description.", required = true)
    private String description;

    @NotNull
    @Range(min = 5, max = 240)
    @ApiModelProperty(value = "Movie duration in minutes.", required = true)
    private Long duration;

}
