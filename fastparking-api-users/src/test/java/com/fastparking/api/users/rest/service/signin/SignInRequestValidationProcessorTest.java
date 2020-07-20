package com.fastparking.api.users.rest.service.signin;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.fastparking.api.users.rest.service.TestData.buildTestStringSignInRequest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SignInRequestValidationProcessorTest {

    private Exchange exchange;
    private SignInRequestValidationProcessor signInRequestValidationProcessor;

    @BeforeEach
    public void setUp() {
        CamelContext ctx = new DefaultCamelContext();
        exchange = new DefaultExchange(ctx);
        signInRequestValidationProcessor = new SignInRequestValidationProcessor();
    }

    @Test
    public void givenProcessorWhenProcessOnCalledWithNoSignInRequestExpextJSONException() {
        assertThrows(JSONException.class, () -> {
           signInRequestValidationProcessor.process(exchange);
        });
    }

    @Test
    public void givenValidRequestWhenProcessorIsCalledExpectNoValidationErrors() throws Exception {
        buildTestStringSignInRequest(exchange, true);
        signInRequestValidationProcessor.process(exchange);
        assertNotNull(exchange.getIn().getBody());
    }

    @Test
    public void givenInvalidRequestWhenProcessorIsCalledExpectValidationException() {
        buildTestStringSignInRequest(exchange, false);
        assertThrows(ValidationException.class, () -> {
           signInRequestValidationProcessor.process(exchange);
        });

    }
}
