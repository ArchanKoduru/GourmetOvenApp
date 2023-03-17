package com.gourmetoven.gourmetapp.exception;

public class DataNotAvailableException extends RuntimeException {
    public DataNotAvailableException() {
        super();
    }

    public DataNotAvailableException(String message) {
        super(message);
    }
}
