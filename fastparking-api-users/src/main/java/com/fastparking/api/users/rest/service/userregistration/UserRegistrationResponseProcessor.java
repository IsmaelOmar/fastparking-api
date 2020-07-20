package com.fastparking.api.users.rest.service.userregistration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.lib.builder.responsebuilder.ResponseBuilder;
import com.fastparking.api.schema.UserRegistrationRequest;
import com.fastparking.api.schema.UserRegistrationResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class UserRegistrationResponseProcessor implements Processor {

    private static final Logger LOGGER = getLogger(UserRegistrationResponseProcessor.class);

    @Autowired
    private ResponseBuilder responseBuilder;

    @Override
    public void process(Exchange exchange) throws Exception {
        UserRegistrationRequest userRegistrationRequest = (UserRegistrationRequest) exchange.getIn().getHeaders().get(ORIGINAL_REQUEST);

        if(userRegistrationRequest != null) {
            exchange.getIn().getHeaders().put(ORIGINAL_REQUEST, userRegistrationRequest);
            UserRegistrationResponse userRegistrationResponse = responseBuilder.buildUserRegistrationResponse(exchange);

            exchange.getIn().setBody(responseToJson(userRegistrationResponse));
        } else {
            LOGGER.error("User registration request cannot be null: ");
            throw new IllegalArgumentException("User registration request cannot be null");
        }
    }

    public JSONObject responseToJson(UserRegistrationResponse userRegistrationResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String responseAsJsonString = mapper.writeValueAsString(userRegistrationResponse);

        return new JSONObject(responseAsJsonString);
    }

    public void setResponseBuilder(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }
}
