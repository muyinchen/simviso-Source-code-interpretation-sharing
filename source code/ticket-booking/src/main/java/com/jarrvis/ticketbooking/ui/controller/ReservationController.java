package com.jarrvis.ticketbooking.ui.controller;


import com.jarrvis.ticketbooking.application.service.ReservationService;
import com.jarrvis.ticketbooking.ui.dto.request.ConfirmReservationCommand;
import com.jarrvis.ticketbooking.ui.dto.request.CreateReservationCommand;
import com.jarrvis.ticketbooking.ui.dto.response.ReservationResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;


/**
 * REST API endpoint for multiplex reservation related operations
 */
@Slf4j
@RestController
@RequestMapping(value = "/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * REST operation to create new reservation in multiplex
     *
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "Create ticket reservation in multiplex")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation created"),
            @ApiResponse(code = 409, message = "Movie does not exist or room does not exist or screening overlaps with existing screenings"),
            @ApiResponse(code = 422, message = "Create reservation parameters contains validation errors"),
    })
    public Mono<ResponseEntity<ReservationResource>> reserve(
            @ApiParam(value = "Details to create a reservation") @RequestBody @Valid CreateReservationCommand createReservationCommand,
            BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {

        log.debug("Create ticket reservation request received: {}", createReservationCommand);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getMethod("reserve", CreateReservationCommand.class, BindingResult.class), 0),
                    bindingResult);
        }
        return reservationService.reserve(
                createReservationCommand.getScreeningId(),
                createReservationCommand.getName(),
                createReservationCommand.getSurname(),
                createReservationCommand.getTickets())

                .flatMap((resource) -> Mono.just(ResponseEntity.created(URI.create("/reservations")).body(resource)));
    }

    /**
     * REST operation to confirm reservation in multiplex
     */
    @PatchMapping()
    @ApiOperation(value = "Confirm reservation in multiplex")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reservation confirmed"),
            @ApiResponse(code = 404, message = "Reservation not found"),
    })
    public Mono<ReservationResource> confirm(
            @ApiParam(value = "Details to create a reservation") @RequestBody @Valid ConfirmReservationCommand confirmReservationCommand,
            BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {

        log.debug("Confirm ticket reservation request received: {}", confirmReservationCommand);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getMethod("confirm", ConfirmReservationCommand.class, BindingResult.class), 0),
                    bindingResult);
        }
        return reservationService.confirm(confirmReservationCommand.getReservationId(), confirmReservationCommand.getToken());
    }

}
