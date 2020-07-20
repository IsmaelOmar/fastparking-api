package com.fastparking.api.lib.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.fastparking.api.lib.commons.constants.DataConstants.VALIDATION_ERROR_MESSAGE_CHARACTER_LIMIT;

/**
 * Utility class for the FastParking Application
 */
public class FastParkingUtil {

    private FastParkingUtil() {

    }

    /**
     * This method truncates the error message in the logs
     * @param message message from Exception class
     * @return truncated message
     */
    public static String truncateErrorMessage(String message) {
        message = message.substring(0, Math.min(message.length(), VALIDATION_ERROR_MESSAGE_CHARACTER_LIMIT));
        return message;
    }


    /**
     * This method returns the current date as a string in the format stated below
     * @return current date as a string
     */
    public static String formatCurrentDateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

}
