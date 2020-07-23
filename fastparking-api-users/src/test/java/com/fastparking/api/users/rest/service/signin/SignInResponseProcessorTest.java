package com.fastparking.api.users.rest.service.signin;

import com.fastparking.api.lib.builder.responsebuilder.ResponseBuilder;
import com.fastparking.api.lib.builder.responsebuilder.ResponseBuilderImpl;
import com.fastparking.api.schema.UserSignInRequest;
import com.fastparking.api.schema.UserSignInResponse;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SignInResponseProcessorTest {

    private ResponseBuilder responseBuilder;
    private Exchange exchange;
    private SignInResponseProcessor signInResponseProcessor;

    @BeforeEach
    public void setUp() {
        CamelContext ctx = new DefaultCamelContext();
        exchange = new DefaultExchange(ctx);
        signInResponseProcessor = new SignInResponseProcessor();
        responseBuilder = mock(ResponseBuilderImpl.class);
    }

    @Test
    public void givenOriginalRequestIsNullWhenProcessMethodIsCalledThenExpectIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            signInResponseProcessor.process(exchange);
        });
    }

    @Test
    public void givenOriginalRequestIsNotNullWhenProcessMethodIsCalledThenExpectValidResponseInExchangeBody() throws Exception {
        UserSignInRequest userSignInRequest = new UserSignInRequest();
        userSignInRequest.setUsername("MyNameIsJeff");
        userSignInRequest.setPassword("MyNameIsJeff");
        exchange.getIn().getHeaders().put(ORIGINAL_REQUEST, userSignInRequest);

        UserSignInResponse userSignInResponse = new UserSignInResponse();
        userSignInResponse.setResult("Signed In Successfully");

        when(responseBuilder.buildUserSignInResponse(exchange)).thenReturn(userSignInResponse);

        signInResponseProcessor.setResponseBuilder(responseBuilder);
        signInResponseProcessor.process(exchange);

        assertNotNull(exchange.getIn().getBody());
    }
}
