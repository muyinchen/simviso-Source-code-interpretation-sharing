package com.jarrvis.ticketbooking.ui.dto.request

import spock.lang.Specification
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.time.LocalDateTime

class AddNewScreeningRequestSpec extends Specification {


    private static Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()

    }

    def "should not be possible to add new screening without movie name"() {
        setup:
            def addNewScreeningRequest = AddNewScreeningRequest.builder()
                    .movieName("")
                    .roomName("Dream")
                    .startTime(LocalDateTime.now().plusDays(5))
                    .build()


        when:
            Set<ConstraintViolation<AddNewScreeningRequest>> validationResults = validator.validate(addNewScreeningRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "movieName" }
    }

    def "should not be possible to add new screening without room name"() {
        setup:
            def addNewScreeningRequest = AddNewScreeningRequest.builder()
                    .movieName("Joker")
                    .roomName("")
                    .startTime(LocalDateTime.now().plusDays(5))
                    .build()


        when:
            Set<ConstraintViolation<AddNewScreeningRequest>> validationResults = validator.validate(addNewScreeningRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "roomName" }
    }
}