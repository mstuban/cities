package com.infinum.cities.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotAddCityException extends RuntimeException {

    public CannotAddCityException(String errorMessage) {
        super(errorMessage);
    }
}

