package com.jarrvis.ticketbooking.ui.controller;


import com.jarrvis.ticketbooking.application.service.MovieRoomService;
import com.jarrvis.ticketbooking.ui.dto.request.AddNewRoomRequest;
import com.jarrvis.ticketbooking.ui.dto.response.RoomResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;


/**
 * REST API endpoint for multiplex rooms related operations
 */
@Slf4j
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping(value = "/rooms")
public class RoomController {

    private final MovieRoomService movieRoomService;

    public RoomController(
            final MovieRoomService movieRoomService) {
        this.movieRoomService = movieRoomService;
    }

    /**
     * REST operation to add new movie to multiplex offer
     */
    @PostMapping()
    @ApiOperation(value = "Adds new multiplex room")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Room added to multiplex"),
            @ApiResponse(code = 401, message = "Missing required authorization"),
            @ApiResponse(code = 409, message = "Room with such name already exists"),
            @ApiResponse(code = 428, message = "Add new room body parameters contains validation errors"),
    })
    public Mono<ResponseEntity<RoomResource>> add(
            @ApiParam(value = "Room details to be added to multiplex") @RequestBody @Valid AddNewRoomRequest addNewRoomRequest,
            BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {

        log.debug("Add new movie request received: {}", addNewRoomRequest);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getMethod("add", AddNewRoomRequest.class, BindingResult.class), 0),
                    bindingResult);
        }

        return movieRoomService.addNewRoom(addNewRoomRequest.getName(), addNewRoomRequest.getRows(), addNewRoomRequest.getSeatsPerRow())
                .flatMap((status) -> Mono.just(ResponseEntity.created(URI.create("/rooms")).build()));
    }

    /**
     * REST operation to add new movie to multiplex offer
     */
    @GetMapping()
    @ApiOperation(value = "List multiplex rooms")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rooms at multiplex"),
            @ApiResponse(code = 401, message = "Missing required authorization"),
    })
    public Flux<RoomResource> get() {
        return this.movieRoomService.listAllRooms();
    }


}
