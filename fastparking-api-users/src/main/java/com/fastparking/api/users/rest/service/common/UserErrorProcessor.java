package com.fastparking.api.users.rest.service.common;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import static com.fastparking.api.lib.commons.constants.DataConstants.TECHNICAL_ERROR;
import static com.fastparking.api.lib.commons.constants.DataConstants.TECHNICAL_ERROR_CD;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Component
public class UserErrorProcessor extends ErrorProcessor {

    @Override
    protected int getHTTPResponseStatusCd(Exchange exchange) {
        return INTERNAL_SERVER_ERROR.getStatusCode();
    }

    @Override
    protected String getErrorStatusCd(Exchange exchange) {
        return TECHNICAL_ERROR_CD;
    }

    @Override
    protected String getErrorMessage(Exchange exchange) {
        return TECHNICAL_ERROR;
    }
}
