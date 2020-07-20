package com.fastparking.api.users.rest.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.schema.Error;
import org.apache.camel.Exchange;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static com.fastparking.api.lib.commons.constants.DataConstants.INVALID_REQUEST_ERROR_CD;
import static com.fastparking.api.lib.commons.constants.DataConstants.INVALID_REQUEST_ERROR;
import static com.fastparking.api.lib.commons.constants.DataConstants.INVALID_RESPONSE_ERROR_CD;
import static com.fastparking.api.lib.commons.constants.DataConstants.INVALID_RESPONSE_ERROR;
import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;
import static com.fastparking.api.lib.commons.constants.DataConstants.RESULT;
import static com.fastparking.api.lib.commons.util.FastParkingUtil.truncateErrorMessage;
import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class UserRegistrationValidationErrorProcessor extends ErrorProcessor {
    private static final Logger LOGGER = getLogger(UserRegistrationValidationErrorProcessor.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected int getHTTPResponseStatusCd(Exchange exchange) {
        int responseCd = BAD_REQUEST.getStatusCode();
        if (isResponseValidationError(exchange)) {
            responseCd = INTERNAL_SERVER_ERROR.getStatusCode();
        }
        return responseCd;
    }

    @Override
    protected String getErrorStatusCd(Exchange exchange) {
        String statusCd = INVALID_REQUEST_ERROR_CD;
        if (isResponseValidationError(exchange)) {
            statusCd = INVALID_RESPONSE_ERROR_CD;
        }
        return statusCd;
    }

    @Override
    protected String getErrorMessage(Exchange exchange) {
        String message;
        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) instanceof ValidationException) {
            ValidationException ex = (ValidationException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
            message = String.join(",", ex.getAllMessages());
        } else {
            JSONException exception = (JSONException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
            message = exception.getMessage();
        }

        message = truncateErrorMessage(message);

        if (isResponseValidationError(exchange)) {
            message = INVALID_RESPONSE_ERROR + message;
        }else{
            message = INVALID_REQUEST_ERROR + message;
        }
        return message;
    }

    @Override
    protected void verifyOriginalRequest(Exchange exchange) {
        if (!ofNullable(exchange.getIn().getHeader(ORIGINAL_REQUEST)).isPresent()) {
            LOGGER.error("Search request cannot be null: ");
            try {
                exchange.getIn().setHeader(ORIGINAL_REQUEST, mapper.writeValueAsString(buildErrorEmptyRequest()));
            } catch (JsonProcessingException e) {
                LOGGER.error("Search request cannot be mapped: ", e);
            }
        }
    }

    @Override
    protected Error getSearchResponse(Exchange exchange) {
        return responseBuilder.buildUserRegistrationErrorResponseForValidationException(exchange);
    }

    private boolean isResponseValidationError(Exchange exchange) {
        return ofNullable(exchange.getIn().getBody()).isPresent() && exchange.getIn().getBody().toString().contains(RESULT);
    }

    protected void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
