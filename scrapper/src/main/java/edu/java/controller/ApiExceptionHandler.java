package edu.java.controller;

import edu.java.dto.bot.response.ApiErrorResponse;
import edu.java.exception.api.BadRequestException;
import edu.java.exception.api.NotFoundException;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException e) {
        ApiErrorResponse response = new ApiErrorResponse(
            e.getDescription(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            getStackTaraceList(e)
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private static List<String> getStackTaraceList(Throwable e) {
        var result = new ArrayList<String>();

        for (var elem : e.getStackTrace()) {
            result.add(elem.toString());
        }

        return result;
    }
}
