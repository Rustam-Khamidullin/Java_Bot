package edu.java.controller;

import edu.java.dto.bot.response.ApiErrorResponse;
import edu.java.exception.api.BadRequestException;
import edu.java.exception.api.NotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        BadRequestException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(Exception e) {
        return handleException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(Exception e) {
        return handleException(e, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ApiErrorResponse> handleException(Exception e, HttpStatus httpStatus) {
        ApiErrorResponse response = new ApiErrorResponse(
            httpStatus.getReasonPhrase(),
            httpStatus.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
        return new ResponseEntity<>(response, httpStatus);
    }
}
