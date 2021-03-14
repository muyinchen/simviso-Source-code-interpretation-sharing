package com.jarrvis.ticketbooking.ui.dto.request

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.time.LocalDateTime


class CreateReservationCommandSpec extends Specification {

    private static Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()

    }

    @Unroll
    def "customer name should be valid"() {
        expect:
            def createReservationCommand = CreateReservationCommand.builder()
                    .name(name)
                    .surname("Test")
                    .screeningId("1234")
                    .build()

            Set<ConstraintViolation<CreateReservationCommand>> validationResults = validator.validate(createReservationCommand)
            result == validationResults.any { it.getPropertyPath().first().name == "name" }
        where:
            name          | result
            "Jan"         | false
            "Łukasz"      | false
            "test"        | true
            "Jan123"      | true
            "JAn"         | true
            "J!an"        | true
            "Jan-Andrzej" | true
    }

    @Unroll
    def "customer surname should be valid"() {
        expect:
            def createReservationCommand = CreateReservationCommand.builder()
                    .name("Jan")
                    .surname(surname)
                    .screeningId("1234")
                    .build()

            Set<ConstraintViolation<CreateReservationCommand>> validationResults = validator.validate(createReservationCommand)
            result == validationResults.any { it.getPropertyPath().first().name == "surname" }
        where:
            surname            | result
            "Kowalski"         | false
            "Kowalski-Łukasik" | false
            "test"             | true
            "test-test"        | true
            "test-Test"        | true
            "Jan123"           | true
            "JAn-Jan"          | true
            "J!an"             | true
            "Jan-Andrzej-Jan"  | true
    }
}