package com.es.core.exception;

public class OutOfStockException extends RuntimeException {
    private Long phoneId;

    public OutOfStockException() {
    }

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Long phoneId) {
        super(message);
        this.phoneId = phoneId;
    }

    public Long getPhoneId() {
        return phoneId;
    }
}
