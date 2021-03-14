package com.jarrvis.ticketbooking.application.service

import com.jarrvis.ticketbooking.domain.Movie
import com.jarrvis.ticketbooking.domain.MovieRepository
import com.jarrvis.ticketbooking.ui.exception.AlreadyExistException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.LocalDateTime

class MovieServiceSpec extends Specification {

    private MovieService movieService
    private MovieRepository movieRepository

    def setup() {
        movieRepository = Mock(MovieRepository)
        movieService = new MovieService(movieRepository)
    }

    def "Should list all movies"() {
        given:
            def movie = new Movie("Joker", "Joker", 120)
            movieRepository.findAll() >> Flux.just(movie)
        when:
            def resource = movieService.listAllMovies().blockFirst()
        then:
            resource.name == movie.name
            resource.description == movie.description
            resource.duration == movie.duration
    }

    def "Should not be able to add movie with name that already exist"() {
        given:
            movieRepository.existsByName(_ as String) >> Mono.just(true)

        when:
            movieService.addNewMovie("Joker", "Joker", 120)
                    .block()
        then:
            thrown(AlreadyExistException)

    }

    def "Should be able to add movie"() {
        given:
            movieRepository.existsByName(_ as String) >> Mono.just(false)
            movieRepository.save(_ as Movie) >> { Movie movie -> Mono.just(movie )}

        when:
            def result = movieService.addNewMovie("Joker", "Joker", 120)
                    .block()
        then:
            result
    }
}