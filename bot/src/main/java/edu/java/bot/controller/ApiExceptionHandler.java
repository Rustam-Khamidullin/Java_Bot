package edu.java.bot.controller;

import edu.java.bot.dto.api.response.ApiErrorResponse;
import edu.java.bot.exception.api.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        ApiErrorResponse response = new ApiErrorResponse(
            e.getDescription(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            getStackTaraceList(e)
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private static List<String> getStackTaraceList(Throwable e) {
        var result = new ArrayList<String>();

        for (var elem : e.getStackTrace()) {
            result.add(elem.toString());
        }

        return result;
    }
}
