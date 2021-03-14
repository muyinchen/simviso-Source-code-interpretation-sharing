package com.jarrvis.ticketbooking.ui.dto.request

import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

class ConfirmReservationCommandSpec extends Specification {

    private static Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()

    }

    def "should not be possible to confirm reservation without token"() {
        given:
            def confirmReservationCommand = ConfirmReservationCommand.builder()
                    .reservationId("test")
                    .token("")
                    .build()
        when:
            Set<ConstraintViolation<ConfirmReservationCommand>> validationResults = validator.validate(confirmReservationCommand)
        then:
            validationResults.any { it.getPropertyPath().first().name == "token" }
    }

    def "should not be possible to confirm reservation without reservation id"() {
        given:
            def confirmReservationCommand = ConfirmReservationCommand.builder()
                    .reservationId("")
                    .token("test")
                    .build()
        when:
            Set<ConstraintViolation<ConfirmReservationCommand>> validationResults = validator.validate(confirmReservationCommand)
        then:
            validationResults.any { it.getPropertyPath().first().name == "reservationId" }
    }
}