package com.fastparking.api.lib.commons.constants;

public class DataConstants {

    public static final String CUSTOMER_SIGN_UP_REQUEST_SCHEMA = "customerSignUpRequest";
    public static final String BARBER_SIGN_UP_REQUEST_SCHEMA = "barberSignUpRequest";
    public static final String ORIGINAL_REQUEST = "OriginalRequest";
    public static final String TRACKING_ID = "trackingId";
    public static final String CORRELATION_ID = "correlationId";
    public static final String MESSAGE_ID = "messageId";
    public static final String SCHEMA_FILE_NAME = "components.json";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String ERROR_CODE = "errorCode";
    public static final String RESULT = "{\"result\":";
    public static final String SIGN_IN_RESULT = "SignInResult";

    // Error codes & Error Messages
    public static final String DATABASE_AUTHENTICATION_ERROR = "Database Authentication Error";
    public static final String DATABASE_AUTHENTICATION_ERROR_CD = "2010";
    public static final String DATABASE_UNAVAILABLE_ERROR = "Database Unavailable";
    public static final String DATABASE_UNAVAILABLE_ERROR_CD = "2030";
    public static final String INVALID_REQUEST_ERROR = "Invalid Request JSON: ";
    public static final String INVALID_REQUEST_ERROR_CD = "2020";
    public static final String INVALID_RESPONSE_ERROR = "Invalid Response JSON ";
    public static final String INVALID_RESPONSE_ERROR_CD = "2040";
    public static final String DATA_NOT_FOUND_ERROR = "Data not in expected structure or format";
    public static final String DATA_NOT_FOUND_ERROR_CD = "2050";
    public static final String USER_REGISTRATION_REQUEST_VALIDATION_ERROR = "Schema Error: User Registration Request";
    public static final String BARBER_SIGN_UP_REQUEST_VALIDATION_ERROR = "Schema Error: Barber Sign Up Request";
    public static final String TECHNICAL_ERROR = "Technical Error";
    public static final String TECHNICAL_ERROR_CD = "9000";


    // Limits
    public static final int VALIDATION_ERROR_MESSAGE_CHARACTER_LIMIT = 900;

}
