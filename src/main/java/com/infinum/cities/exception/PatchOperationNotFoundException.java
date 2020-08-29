package com.infinum.cities.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatchOperationNotFoundException extends RuntimeException {

    public PatchOperationNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
