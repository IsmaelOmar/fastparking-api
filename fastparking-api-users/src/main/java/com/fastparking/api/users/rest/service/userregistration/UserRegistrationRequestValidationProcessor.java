package com.fastparking.api.users.rest.service.userregistration;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.schema.UserRegistrationRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.everit.json.schema.ValidationException;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

import java.io.IOException;

import static com.fastparking.api.lib.commons.constants.DataConstants.TECHNICAL_ERROR_CD;
import static com.fastparking.api.lib.commons.constants.DataConstants.TRACKING_ID;
import static com.fastparking.api.lib.commons.constants.DataConstants.CORRELATION_ID;
import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;
import static com.fastparking.api.lib.commons.constants.DataConstants.SCHEMA_FILE_NAME;
import static com.fastparking.api.lib.commons.constants.DataConstants.TECHNICAL_ERROR;
import static com.fastparking.api.lib.commons.constants.DataConstants.MESSAGE_ID;
import static com.fastparking.api.lib.commons.constants.DataConstants.INVALID_REQUEST_ERROR_CD;
import static com.fastparking.api.lib.commons.constants.DataConstants.INVALID_REQUEST_ERROR;
import static com.fastparking.api.lib.commons.util.MessageIdGenerator.getMessageId;
import static com.fastparking.api.lib.schema.validation.utils.SchemaValidator.validateAgainstSchema;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class UserRegistrationRequestValidationProcessor implements Processor {

    private static final Logger LOGGER = getLogger(UserRegistrationRequestValidationProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        if(exchange.getIn().getBody() != null) {
            try {
                MDC.put(TRACKING_ID, exchange.getIn().getHeader(TRACKING_ID, String.class));
                MDC.put(CORRELATION_ID, exchange.getIn().getHeader(CORRELATION_ID, String.class));

                String userRegistrationRequestAsString = (String) exchange.getIn().getBody();
                validateAgainstSchema(SCHEMA_FILE_NAME, userRegistrationRequestAsString, "userRegistrationRequest");

                UserRegistrationRequest userRegistrationRequest = mapper.readValue((String) exchange.getIn().getBody(), UserRegistrationRequest.class);
                exchange.getIn().setBody(userRegistrationRequest);
                exchange.getIn().setHeader(ORIGINAL_REQUEST, userRegistrationRequest);
                exchange.getIn().setHeader(MESSAGE_ID, getMessageId());
            } catch (JsonMappingException e) {
                LOGGER.error("User registration request cannot be mapped : " + e);
                String validationError = "\"<" + e.getMessage() + ">\" ";
                throw new FastParkingApplicationException(INVALID_REQUEST_ERROR + validationError, Response.Status.BAD_REQUEST.getStatusCode(), INVALID_REQUEST_ERROR_CD);
            } catch (IOException ioe) {
                LOGGER.error("User registration cannot be mapped : " + ioe);
                throw new FastParkingApplicationException(TECHNICAL_ERROR, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), TECHNICAL_ERROR_CD);
            } catch (ValidationException ex) {
                LOGGER.error("Error in validating request body of User Registration to JSON Schema: " + ex);
                throw new FastParkingApplicationException(INVALID_REQUEST_ERROR, Response.Status.BAD_REQUEST.getStatusCode(), INVALID_REQUEST_ERROR_CD);
            }
        }
    }
}
