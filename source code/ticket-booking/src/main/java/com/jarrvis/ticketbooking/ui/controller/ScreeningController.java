package com.jarrvis.ticketbooking.ui.controller;


import com.jarrvis.ticketbooking.application.service.ScreeningService;
import com.jarrvis.ticketbooking.ui.dto.request.AddNewScreeningRequest;
import com.jarrvis.ticketbooking.ui.dto.request.SearchScreeningRequest;
import com.jarrvis.ticketbooking.ui.dto.response.ScreeningResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * REST API endpoint for multiplex screenings related operations
 */
@Slf4j
@RestController
@RequestMapping(value = "/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(
            final ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    /**
     * REST operation to add new screening to multiplex offer
     */
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "Adds new screening to multiplex offer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Screening added to multiplex offer"),
            @ApiResponse(code = 401, message = "Missing required authorization"),
            @ApiResponse(code = 409, message = "Movie does not exist or room does not exist or screening overlaps with existing screenings"),
            @ApiResponse(code = 422, message = "Add new screening body parameters contains validation errors"),
    })
    public Mono<ResponseEntity> add(
            @ApiParam(value = "Screening details to be added to offer") @RequestBody @Valid AddNewScreeningRequest addNewScreeningRequest,
            BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {

        log.debug("Add new screening request received: {}", addNewScreeningRequest);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getMethod("add", AddNewScreeningRequest.class, BindingResult.class), 0),
                    bindingResult);
        }

        return screeningService.addNewScreening(addNewScreeningRequest.getStartTime(), addNewScreeningRequest.getMovieName(), addNewScreeningRequest.getRoomName())
                .flatMap((resource) -> Mono.just(ResponseEntity.created(URI.create("/screenings")).body(resource)));
    }

    /**
     * REST operation to search for screenings in multiplex offer
     */
    @GetMapping()
    @ApiOperation(value = "Search for screenings in multiplex offer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Screenings found in multiplex offer"),
            @ApiResponse(code = 422, message = "Search screenings parameters contain validation errors"),
    })
    public Flux<ScreeningResource> search(
            @ApiParam(value = "Screening details to be added to offer") @Valid SearchScreeningRequest searchScreeningRequest,
            BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {

        log.debug("Search screenings request received: {}", searchScreeningRequest);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getMethod("search", SearchScreeningRequest.class, BindingResult.class), 0),
                    bindingResult);
        }

        return screeningService.searchForScreenings(searchScreeningRequest.getStartTime(), searchScreeningRequest.getEndTime());
    }

}
