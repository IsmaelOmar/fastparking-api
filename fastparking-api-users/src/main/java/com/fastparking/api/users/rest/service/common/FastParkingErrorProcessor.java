package com.fastparking.api.users.rest.service.common;

import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class FastParkingErrorProcessor extends ErrorProcessor {

    @Override
    protected int getHTTPResponseStatusCd(Exchange exchange) {
        return ((FastParkingApplicationException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT)).getStatusCode();
    }

    @Override
    protected String getErrorStatusCd(Exchange exchange) {
        return ((FastParkingApplicationException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT)).getErrorCode();
    }

    @Override
    protected String getErrorMessage(Exchange exchange) {
        String message = ((FastParkingApplicationException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT)).getMessage();
        return message.replace("com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException: ", "");
    }
}
