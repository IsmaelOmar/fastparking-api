package com.fastparking.api.users.rest.service.signin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.lib.builder.responsebuilder.ResponseBuilder;
import com.fastparking.api.schema.UserRegistrationResponse;
import com.fastparking.api.schema.UserSignInRequest;
import com.fastparking.api.schema.UserSignInResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SignInResponseProcessor implements Processor {

    private static final Logger LOGGER = getLogger(SignInResponseProcessor.class);

    @Autowired
    private ResponseBuilder responseBuilder;

    @Override
    public void process(Exchange exchange) throws Exception{

        UserSignInRequest userSignInRequest = (UserSignInRequest) exchange.getIn().getHeaders().get(ORIGINAL_REQUEST);

        if (userSignInRequest != null) {
            exchange.getIn().getHeaders().put(ORIGINAL_REQUEST, userSignInRequest);
            UserSignInResponse userSignInResponse = responseBuilder.buildUserSignInResponse(exchange);

            exchange.getIn().setBody(responseToJson(userSignInResponse));
        } else {
            LOGGER.error("User Sign in request cannot be null: ");
            throw new IllegalArgumentException("User Sign in request cannot be null ");
        }

    }

    public JSONObject responseToJson(UserSignInResponse userSignInResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String responseAsJsonString = mapper.writeValueAsString(userSignInResponse);

        return new JSONObject(responseAsJsonString);
    }

    public void setResponseBuilder(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }
}
