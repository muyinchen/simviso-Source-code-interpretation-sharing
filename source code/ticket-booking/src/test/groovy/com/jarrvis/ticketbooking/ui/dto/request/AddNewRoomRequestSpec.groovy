package com.jarrvis.ticketbooking.ui.dto.request


import spock.lang.Specification
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

class AddNewRoomRequestSpec extends Specification {

    private static Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()

    }

    def "should not be possible to add new room without name"() {
        setup:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("")
                    .rows(AddNewRoomRequest.MAX_NUMBER_OF_ROWS -1)
                    .seatsPerRow(AddNewRoomRequest.MAX_NUMBER_OF_SEATS_PER_ROW -1)
                    .build()

        when:
            Set<ConstraintViolation<AddNewRoomRequest>> validationResults = validator.validate(addNewRoomRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "name" }
    }

    def "should not be possible to add new room with number of rows more than max"() {
        setup:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("Joker")
                    .rows(AddNewRoomRequest.MAX_NUMBER_OF_ROWS + 1)
                    .seatsPerRow(AddNewRoomRequest.MAX_NUMBER_OF_SEATS_PER_ROW -1)
                    .build()

        when:
            Set<ConstraintViolation<AddNewRoomRequest>> validationResults = validator.validate(addNewRoomRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "rows" }
    }

    def "should not be possible to add new room with 0 rows"() {
        setup:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("Joker")
                    .rows(0)
                    .seatsPerRow(AddNewRoomRequest.MAX_NUMBER_OF_SEATS_PER_ROW -1)
                    .build()

        when:
            Set<ConstraintViolation<AddNewRoomRequest>> validationResults = validator.validate(addNewRoomRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "rows" }
    }

    def "should not be possible to add new room with number of seats more than max"() {
        setup:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("Joker")
                    .rows(AddNewRoomRequest.MAX_NUMBER_OF_ROWS - 1)
                    .seatsPerRow(AddNewRoomRequest.MAX_NUMBER_OF_SEATS_PER_ROW +1)
                    .build()

        when:
            Set<ConstraintViolation<AddNewRoomRequest>> validationResults = validator.validate(addNewRoomRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "seatsPerRow" }
    }

    def "should not be possible to add new room with 0 seats"() {
        setup:
            def addNewRoomRequest = AddNewRoomRequest.builder()
                    .name("Joker")
                    .rows(AddNewRoomRequest.MAX_NUMBER_OF_ROWS - 1)
                    .seatsPerRow(0)
                    .build()

        when:
            Set<ConstraintViolation<AddNewRoomRequest>> validationResults = validator.validate(addNewRoomRequest)
        then:
            validationResults.any { it.getPropertyPath().first().name == "seatsPerRow" }
    }
}