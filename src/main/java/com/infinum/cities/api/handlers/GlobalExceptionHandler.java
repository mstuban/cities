package com.infinum.cities.api.handlers;

import com.infinum.cities.exception.CannotAddCityException;
import com.infinum.cities.exception.CityNotFoundException;
import com.infinum.cities.exception.UserNotFoundException;
import com.infinum.cities.model.errors.ErrorMessage;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    ResponseEntity<ErrorMessage> handleCityNotFoundException(CityNotFoundException e) {
        return new ResponseEntity<>(
            new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalDateTime.now()
            ),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(
            new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalDateTime.now()
            ),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
            new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                Arrays.toString(e.getBindingResult().getFieldErrors().stream().map(
                    DefaultMessageSourceResolvable::getDefaultMessage
                ).toArray()),
                LocalDateTime.now()
            ),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return new ResponseEntity<>(
            new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
            ),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleMalformedJwtException(MalformedJwtException e) {
        return new ResponseEntity<>(
            new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                LocalDateTime.now()
            ),
            HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleCannotAddCityException(CannotAddCityException e) {
        return new ResponseEntity<>(
            new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
            ),
            HttpStatus.BAD_REQUEST);
    }

}
