package com.fastparking.api.users.rest.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.lib.builder.responsebuilder.ResponseBuilder;
import com.fastparking.api.schema.Error;
import com.fastparking.api.schema.UserSignInRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static com.fastparking.api.lib.commons.constants.DataConstants.*;
import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class ErrorProcessor implements Processor {

    private static final Logger LOGGER = getLogger(ErrorProcessor.class);

    @Autowired
    protected ResponseBuilder responseBuilder;

    @Override
    public void process(Exchange exchange) throws Exception {
       Error error = null;

        try {
            LOGGER.error(getStackTrace((Throwable) exchange.getProperty(Exchange.EXCEPTION_CAUGHT)));
            verifyOriginalRequest(exchange);
            String message = getErrorMessage(exchange);
            String code = getErrorStatusCd(exchange);
            int httpResponseCd = getHTTPResponseStatusCd(exchange);

            exchange.getIn().getHeaders().put(HTTP_RESPONSE_CODE, httpResponseCd);
            exchange.getIn().getHeaders().put(ERROR_CODE, code);
            exchange.getIn().getHeaders().put(ERROR_MESSAGE, message);
            error = getSearchResponse(exchange);
            exchange.getIn().setBody(error);
        } catch (Exception e) {
            LOGGER.error("Technical error... needs to be fix :: " + e);
            exchange.getIn().getHeaders().put(HTTP_RESPONSE_CODE, INTERNAL_SERVER_ERROR.getStatusCode());
            exchange.getIn().getHeaders().put(ERROR_MESSAGE, TECHNICAL_ERROR);
            exchange.getIn().getHeaders().put(ERROR_CODE, TECHNICAL_ERROR_CD);
            error = responseBuilder.buildErrorResponse(exchange);
            exchange.getIn().setBody(error);
        }

        exchange.getIn().setBody(responseToJson(error));
        LOGGER.info(exchange.getIn().getBody().toString());

    }

    private static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    protected void verifyOriginalRequest(Exchange exchange) {
        UserSignInRequest originalRequest = exchange.getIn().getHeader(ORIGINAL_REQUEST, UserSignInRequest.class);
        if (!ofNullable(originalRequest).isPresent()) {
            LOGGER.error("Search request cannot be null or invalid: ");
            exchange.getIn().setHeader(ORIGINAL_REQUEST, buildErrorEmptyRequest());
        }
    }

    protected UserSignInRequest buildErrorEmptyRequest() {
        UserSignInRequest userSignInRequest = new UserSignInRequest();
        userSignInRequest.setUsername("Unknown");
        userSignInRequest.setPassword("Unknown");
        return userSignInRequest;
    }

    protected Error getSearchResponse(Exchange exchange) {
        return responseBuilder.buildErrorResponse(exchange);
    }

    protected JSONObject responseToJson(Error error) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String responseAsJsonString = mapper.writeValueAsString(error);
        JSONObject errorJsonObj = new JSONObject(responseAsJsonString);

        return errorJsonObj;
    }

    /**
     * Sets the Response builder to build response
     * @param responseBuilder
     */
    public void setResponseBuilder(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    protected abstract int getHTTPResponseStatusCd(Exchange exchange);

    protected abstract String getErrorStatusCd(Exchange exchange);

    protected abstract String getErrorMessage(Exchange exchange);
}
