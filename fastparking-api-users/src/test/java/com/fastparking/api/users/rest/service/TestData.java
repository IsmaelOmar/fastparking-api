package com.fastparking.api.users.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.database.hibernate.entity.UserLoginEntity;
import com.fastparking.api.schema.UserSignInRequest;
import org.apache.camel.Exchange;

import java.io.IOException;
import java.util.UUID;

import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;

public class TestData {

    public static void buildTestStringSignInRequest(Exchange exchange, boolean valid) {
        ObjectMapper mapper = new ObjectMapper();
        exchange.getIn().setBody(buildSignInRequest(valid));
    }

    public static void buildTestUserLoginEntityObject(Exchange exchange, UserLoginEntity userLoginEntity) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        exchange.getIn().setBody(userLoginEntity);
    }


    private static String buildSignInRequest(boolean valid) {
        if(valid) {
            return "{" +
                    "\"username\" : \"MyNameIsJeff\"," +
                    "\"password\" : \"MyNameIsJeff\"" +
                    "}";
        } else {
            return "{" +
                    "\"username\" : \"MyNameIsJeff\"," +
                    "\"dummyField\" : \"MyNameIsJeff\"" +
                    "}";
        }
    }


}
