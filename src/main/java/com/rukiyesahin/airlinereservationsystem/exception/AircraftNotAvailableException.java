package com.rukiyesahin.airlinereservationsystem.exception;

public class AircraftNotAvailableException extends RuntimeException {
    public AircraftNotAvailableException(String message) {
        super(message);
    }

    public AircraftNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}