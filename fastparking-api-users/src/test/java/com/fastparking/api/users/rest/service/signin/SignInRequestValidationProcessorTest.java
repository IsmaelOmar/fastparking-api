package com.fastparking.api.users.rest.service.signin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.schema.UserSignInRequest;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.easymock.Mock;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.fastparking.api.users.rest.service.TestData.buildTestStringSignInRequest;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.powermock.api.easymock.PowerMock.createMockAndExpectNew;
import static org.powermock.api.easymock.PowerMock.replay;

import static org.easymock.EasyMock.expect;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SignInRequestValidationProcessor.class)
public class SignInRequestValidationProcessorTest {

    private Exchange exchange;
    private SignInRequestValidationProcessor signInRequestValidationProcessor;

    @Before
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

    //Junit4 test to use PowerMockito
    @org.junit.Test
    public void Test() throws Exception {
        ObjectMapper objectMapper = createMockAndExpectNew(ObjectMapper.class);
        buildTestStringSignInRequest(exchange, true);
        expect(objectMapper.readValue((String) exchange.getIn().getBody(), UserSignInRequest.class)).andThrow(new JsonMappingException((Closeable) () -> { }, "Empty String Passed"));
        replay(objectMapper, ObjectMapper.class);
        assertThrows(FastParkingApplicationException.class, () -> {
            signInRequestValidationProcessor.process(exchange);
        });
    }

    @Test
    public void testSetRequestBodyIOException() throws NoSuchMethodException {
        Method method = SignInRequestValidationProcessor.class.getDeclaredMethod("setRequestBody", Exchange.class);
        method.setAccessible(true);
        String jsonString = "{";
        exchange.getIn().setBody(jsonString);
        assertThrows(InvocationTargetException.class, () -> {
            method.invoke(new SignInRequestValidationProcessor(), exchange);
        });
    }

    @Test
    public void testSetRequestBodyJsonMappingException() throws NoSuchMethodException {
        Method method = SignInRequestValidationProcessor.class.getDeclaredMethod("setRequestBody", Exchange.class);
        method.setAccessible(true);
        String jsonString = "";
        exchange.getIn().setBody(jsonString);
        assertThrows(InvocationTargetException.class, () -> {
            method.invoke(new SignInRequestValidationProcessor(), exchange);
        });
    }

    @Test
    public void testSetRequestBodyWithNullExchangeBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = SignInRequestValidationProcessor.class.getDeclaredMethod("setRequestBody", Exchange.class);
        method.setAccessible(true);
        exchange.getIn().setBody(null);
        method.invoke(new SignInRequestValidationProcessor(), exchange);
        assertNull(exchange.getIn().getBody());

    }
}
