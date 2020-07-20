package com.fastparking.api.lib.builder.responsebuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class BuilderTransformations {

    private static final Logger LOGGER = getLogger(BuilderTransformations.class);
    /**
     * Get the value of a key in a JSONObject
     *
     * @param obj the JSONObject
     * @param key the key name
     * @return String value of JSON key
     */
    public static String getKeyFromJson(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            LOGGER.error("Exception in getKeyFromJson: ", e);
            return null;
        }
    }

}
