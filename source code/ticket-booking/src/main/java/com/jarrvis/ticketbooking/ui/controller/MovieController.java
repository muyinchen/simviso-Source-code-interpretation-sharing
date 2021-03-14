package com.jarrvis.ticketbooking.ui.controller;


import com.jarrvis.ticketbooking.application.service.MovieService;
import com.jarrvis.ticketbooking.ui.dto.request.AddNewMovieRequest;
import com.jarrvis.ticketbooking.ui.dto.response.MovieResource;
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
 * REST API endpoint for multiplex movies related operations
 */
@Slf4j
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(
            final MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * REST operation to add new movie to multiplex offer
     */
    @PostMapping()
    @ApiOperation(value = "Adds new movie to multiplex offer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Movie added to multiplex offer"),
            @ApiResponse(code = 401, message = "Missing required authorization"),
            @ApiResponse(code = 409, message = "Movie with such name already exists"),
            @ApiResponse(code = 422, message = "Add new movie body parameters contains validation errors"),
    })
    public Mono<ResponseEntity<MovieResource>> add(
            @ApiParam(value = "Movie details to be added to offer") @RequestBody @Valid AddNewMovieRequest addNewMovieRequest,
            BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {

        log.debug("Add new movie request received: {}", addNewMovieRequest);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getMethod("add", AddNewMovieRequest.class, BindingResult.class), 0),
                    bindingResult);
        }

        return movieService.addNewMovie(addNewMovieRequest.getName(), addNewMovieRequest.getDescription(), addNewMovieRequest.getDuration())
                .flatMap((status) -> Mono.just(ResponseEntity.created(URI.create("/movies")).build()));
    }

    /**
     * REST operation to list all movies in multiplex offer
     */
    @GetMapping()
    @ApiOperation(value = "List multiplex movies")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rooms at multiplex"),
            @ApiResponse(code = 401, message = "Missing required authorization"),
    })
    public Flux<MovieResource> get() {
        return this.movieService.listAllMovies();
    }
}
