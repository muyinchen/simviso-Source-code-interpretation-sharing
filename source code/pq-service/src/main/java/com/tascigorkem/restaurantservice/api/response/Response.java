package com.tascigorkem.restaurantservice.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    private int statusCode;

    private HttpStatus status;

    private T payload;

    private Object errors;

    private Object metadata;

    public static <T> Response<T> badRequest() {
        Response<T> response = new Response<>();
        setStatusToResponse(response, HttpStatus.BAD_REQUEST);
        return response;
    }

    public static <T> Response<T> ok() {
        Response<T> response = new Response<>();
        setStatusToResponse(response, HttpStatus.OK);
        return response;
    }

    public static <T> Response<T> unauthorized() {
        Response<T> response = new Response<>();
        setStatusToResponse(response, HttpStatus.UNAUTHORIZED);
        return response;
    }

    public static <T> Response<T> exception() {
        Response<T> response = new Response<>();
        setStatusToResponse(response, HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }

    public static <T> Response<T> notFound() {
        Response<T> response = new Response<>();
        setStatusToResponse(response, HttpStatus.NOT_FOUND);
        return response;
    }

    public static <T> Response<T> duplicateEntity() {
        Response<T> response = new Response<>();
        setStatusToResponse(response, HttpStatus.CONFLICT);
        return response;
    }

    private static <T> void setStatusToResponse(Response<T> response, HttpStatus httpStatus) {
        response.setStatusCode(httpStatus.value());
        response.setStatus(httpStatus);
    }

    public void addErrorMsgToResponse(String errorMsg, Exception ex) {
        ResponseError error = new ResponseError()
                .setDetails(errorMsg)
                .setMessage(ex.getMessage())
                .setTimestamp(new Date());
        setErrors(error);
    }

}