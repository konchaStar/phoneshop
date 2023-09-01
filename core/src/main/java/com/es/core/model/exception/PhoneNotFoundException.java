package com.es.core.model.exception;

public class PhoneNotFoundException extends RuntimeException {
    PhoneNotFoundException() {

    }
    PhoneNotFoundException(String message) {
        super(message);
    }
}
