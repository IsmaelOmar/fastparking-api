package com.fastparking.api.lib.commons.exceptions;

public class FastParkingApplicationException extends Exception {
    private final String errorCode;
    private final int statusCode;

    public FastParkingApplicationException(String errorCode, int statusCode) {
        this.errorCode=errorCode;
        this.statusCode=statusCode;
    }

    public FastParkingApplicationException(Exception e, int statusCode, String errorCode) {
        super(e);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public FastParkingApplicationException(String message, int statusCode, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
