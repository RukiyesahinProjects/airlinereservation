package com.rukiyesahin.airlinereservationsystem.exception;

public class FlightNotAvailableException extends RuntimeException {
    public FlightNotAvailableException(String message) {
        super(message);
    }

    public FlightNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}