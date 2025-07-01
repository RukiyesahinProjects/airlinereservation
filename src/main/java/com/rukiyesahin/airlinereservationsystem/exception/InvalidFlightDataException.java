package com.rukiyesahin.airlinereservationsystem.exception;

public class InvalidFlightDataException extends RuntimeException {
    public InvalidFlightDataException(String message) {
        super(message);
    }

    public InvalidFlightDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
