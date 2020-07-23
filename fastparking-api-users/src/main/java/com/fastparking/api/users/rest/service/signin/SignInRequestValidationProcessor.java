package com.fastparking.api.users.rest.service.signin;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.schema.UserSignInRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

import java.io.IOException;

import static com.fastparking.api.lib.commons.constants.DataConstants.TRACKING_ID;
import static com.fastparking.api.lib.commons.constants.DataConstants.CORRELATION_ID;
import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;
import static com.fastparking.api.lib.commons.constants.DataConstants.INVALID_REQUEST_ERROR_CD;
import static com.fastparking.api.lib.commons.constants.DataConstants.SCHEMA_FILE_NAME;
import static com.fastparking.api.lib.commons.constants.DataConstants.MESSAGE_ID;
import static com.fastparking.api.lib.commons.util.MessageIdGenerator.getMessageId;
import static com.fastparking.api.lib.schema.validation.utils.SchemaValidator.validateAgainstSchema;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SignInRequestValidationProcessor implements Processor {

    private static final Logger LOGGER = getLogger(SignInRequestValidationProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        String signInRequestAsString = (String) exchange.getIn().getBody();

        if(!ofNullable(signInRequestAsString).isPresent()) {
          LOGGER.error("User Sign In request cannot be null!");
            signInRequestAsString = "";
        }
        exchange.getIn().getHeaders().put(ORIGINAL_REQUEST, signInRequestAsString);
        validateAgainstSchema(SCHEMA_FILE_NAME, signInRequestAsString, "userSignInRequest");

        setRequestBody(exchange);
    }

    private void setRequestBody(Exchange exchange) throws FastParkingApplicationException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (exchange.getIn().getBody() != null) {
            try {
                MDC.put(TRACKING_ID, exchange.getIn().getHeader(TRACKING_ID, String.class));
                MDC.put(CORRELATION_ID, exchange.getIn().getHeader(CORRELATION_ID, String.class));

                UserSignInRequest userSignInRequest = objectMapper.readValue((String) exchange.getIn().getBody(), UserSignInRequest.class);
                exchange.getIn().setBody(userSignInRequest);
                exchange.getIn().setHeader(ORIGINAL_REQUEST, userSignInRequest);
                exchange.getIn().setHeader(MESSAGE_ID, getMessageId());

            } catch (JsonMappingException e) {
                LOGGER.error("User Sign In Request cannot be mapped, Json Mapping Issue", e);
                throw new FastParkingApplicationException(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode(), INVALID_REQUEST_ERROR_CD);
            } catch (IOException e) {
                LOGGER.error("User Sign In Request cannot be mapped, IO Issue", e);
                throw new FastParkingApplicationException(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode(), INVALID_REQUEST_ERROR_CD);
            }
        }
    }
}

