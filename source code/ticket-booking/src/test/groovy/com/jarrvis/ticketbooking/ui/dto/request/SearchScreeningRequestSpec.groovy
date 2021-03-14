package com.jarrvis.ticketbooking.ui.dto.request

import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.time.LocalDateTime

class SearchScreeningRequestSpec extends Specification {


    private static Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()

    }

    def "should not be possible to search for screening without start or end time"() {
        setup:
            def searchScreeningRequest = SearchScreeningRequest.builder()
                    .startTime(null)
                    .endTime(null)
                    .build()


        when:
            Set<ConstraintViolation<SearchScreeningRequest>> validationResults = validator.validate(searchScreeningRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "startTime" }
            validationResults.any { it.getPropertyPath().first().name == "endTime" }
    }

    def "should not be possible to search for screening with start time after end time"() {
        setup:
            def searchScreeningRequest = SearchScreeningRequest.builder()
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now().minusHours(2))
                    .build()


        when:
            Set<ConstraintViolation<SearchScreeningRequest>> validationResults = validator.validate(searchScreeningRequest)
        then:
            validationResults.any { it.messageTemplate == "Start date cannot be after end date" }
    }
}