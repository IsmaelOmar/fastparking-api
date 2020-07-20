package com.fastparking.api.lib.builder.responsebuilder;

import com.fastparking.api.schema.UserRegistrationResponse;
import com.fastparking.api.schema.UserSignInResponse;
import com.fastparking.api.schema.Error;
import org.apache.camel.Exchange;

public interface ResponseBuilder {

    UserRegistrationResponse buildUserRegistrationResponse(Exchange exchange);

    UserSignInResponse buildUserSignInResponse(Exchange exchange);

    Error buildErrorResponse(Exchange exchange);

    Error buildSignInErrorResponseForValidationException(Exchange exchange);

    Error buildUserRegistrationErrorResponseForValidationException(Exchange exchange);

}
