package com.fastparking.api.lib.schema.validation.utils;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.fastparking.api.lib.commons.constants.DataConstants.USER_REGISTRATION_REQUEST_VALIDATION_ERROR;
import static com.fastparking.api.lib.commons.util.FastParkingUtil.truncateErrorMessage;
import static com.google.common.io.Resources.getResource;

public class SchemaUtil {

    private SchemaUtil() {

    }

    /**
     * loads json schema
     * @param schemaFileName
     * @return schema object
     * @throws IOException
     */
    public static Schema loadSchema(String schemaFileName) throws IOException {
        final URL resource = getResource(schemaFileName);
        String schemaFileContentAsString = IOUtils.toString(resource.openStream(), StandardCharsets.UTF_8.name());
        JSONTokener jsonTokener = new JSONTokener(schemaFileContentAsString);
        JSONObject rawSchema = new JSONObject(jsonTokener);
        return SchemaLoader.load(rawSchema);
    }

    /**
     * formats the validation error for the UserRegistrationRequest
     *
     * @param ex
     * @return
     */
    public static String getUserRegistrationRequestValidationErrorMessage(ValidationException ex) {
        return USER_REGISTRATION_REQUEST_VALIDATION_ERROR + truncateErrorMessage(String.join(",", ex.getAllMessages()).replaceAll("%", "%%"));
    }
}
