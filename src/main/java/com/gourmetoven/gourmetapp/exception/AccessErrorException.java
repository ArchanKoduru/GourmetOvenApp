package com.gourmetoven.gourmetapp.exception;

public class AccessErrorException extends RuntimeException {
    public AccessErrorException() {
        super();
    }

    public AccessErrorException(String message) {
        super(message);
    }
}
