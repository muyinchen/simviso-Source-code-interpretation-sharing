package com.jarrvis.ticketbooking.ui.dto.request

import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

class AddNewMovieRequestSpec extends Specification {

    private static Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()

    }

    def "should not be possible to add new movie without movie name"() {
        setup:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("")
                    .description("Nice movie")
                    .duration(120)
                    .build()

        when:
            Set<ConstraintViolation<AddNewMovieRequest>> validationResults = validator.validate(addNewMovieRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "name" }
    }

    def "should not be possible to add new movie without movie description"() {
        setup:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("Joker")
                    .description("")
                    .duration(120)
                    .build()

        when:
            Set<ConstraintViolation<AddNewMovieRequest>> validationResults = validator.validate(addNewMovieRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "description" }
    }

    def "should not be possible to add new movie without movie duration"() {
        setup:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("Joker")
                    .description("Joker")
                    .duration()
                    .build()

        when:
            Set<ConstraintViolation<AddNewMovieRequest>> validationResults = validator.validate(addNewMovieRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "duration" }
    }

    def "should not be possible to add new movie with duration shorter than 5 minutes"() {
        setup:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("Joker")
                    .description("Joker")
                    .duration(4)
                    .build()

        when:
            Set<ConstraintViolation<AddNewMovieRequest>> validationResults = validator.validate(addNewMovieRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "duration" }
    }

    def "should not be possible to add new movie with duration longer than than 2 hours"() {
        setup:
            def addNewMovieRequest = AddNewMovieRequest.builder()
                    .name("Joker")
                    .description("Joker")
                    .duration(241)
                    .build()

        when:
            Set<ConstraintViolation<AddNewMovieRequest>> validationResults = validator.validate(addNewMovieRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "duration" }
    }
}