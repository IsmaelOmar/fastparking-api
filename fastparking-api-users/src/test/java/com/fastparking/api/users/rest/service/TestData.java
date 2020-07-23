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
        exchange.getIn().setBody(buildSignInRequest(valid));
    }

    public static void buildTestUserLoginEntityObject(Exchange exchange, UserLoginEntity userLoginEntity) {
        exchange.getIn().setBody(userLoginEntity);
    }

    public static void buildTestUserSignInRequestObject(Exchange exchange, boolean valid) {
        UserSignInRequest userSignInRequest = new UserSignInRequest();
        if (valid) {
            userSignInRequest.setUsername("MyNameIsJeff");
            userSignInRequest.setPassword("MyNameIsJeff");
        } else {
            userSignInRequest.setPassword("");
            userSignInRequest.setUsername("MyNameIsJeff");
        }
        exchange.getIn().setBody(userSignInRequest);
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
