package com.fastparking.api.lib.schema.validation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.schema.UserRegistrationRequest;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.fastparking.api.lib.commons.constants.DataConstants.*;
import static com.fastparking.api.lib.schema.validation.utils.SchemaUtil.getUserRegistrationRequestValidationErrorMessage;
import static com.fastparking.api.lib.schema.validation.utils.SchemaUtil.loadSchema;
import static org.slf4j.LoggerFactory.getLogger;

public final class SchemaValidator {

    private static final Logger LOGGER = getLogger(SchemaValidator.class);

    private SchemaValidator() {
        throw new IllegalStateException("Utility Class");
    }


    /**
     * The validateAgainstSchema method is used to validate a JSON string against a JSON schema.
     *
     * @param schemaFileName This is the first parameter to the validateAgainstSchema method
     * @param jsonString     This is the second parameter to the validateAgainstSchema method
     * @param propertySchema This is the parameter to filter the root schema based on root element for search.
     * @return boolean
     * @throws IOException This is required for openStream method of URL class
     */
    public static void  validateAgainstSchema(final String schemaFileName, final String jsonString, String propertySchema) throws IOException {
        Schema schema = loadSchema(schemaFileName);
        ((ObjectSchema) schema).getPropertySchemas().get(propertySchema).validate(new JSONObject(jsonString));

    }

    public static void validateUserRegistrationRequestAgainstSchema(final String schemaFileName, final UserRegistrationRequest userRegistrationRequest) throws FastParkingApplicationException {
        try {
            Schema schema = loadSchema(schemaFileName);
            ((ObjectSchema) schema).getPropertySchemas().get("userRegistrationRequest")
                    .validate(new JSONObject(new ObjectMapper().writeValueAsString(userRegistrationRequest)));
        } catch (ValidationException e) {
            throw new FastParkingApplicationException(getUserRegistrationRequestValidationErrorMessage(e),
                    Response.Status.OK.getStatusCode(), INVALID_REQUEST_ERROR_CD);
        } catch (IOException e) {
           LOGGER.error("Exception in validateUserRegistrationRequestAgainstSchema: " + e);
           throw new FastParkingApplicationException(TECHNICAL_ERROR, Response.Status.OK.getStatusCode(), TECHNICAL_ERROR_CD);
        }
    }
}
