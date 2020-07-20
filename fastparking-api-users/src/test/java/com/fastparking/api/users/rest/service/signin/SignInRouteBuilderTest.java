package com.fastparking.api.users.rest.service.signin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SignInRouteBuilderTest {

    @Test
    public void testConfigure() throws Exception {
        SignInRouteBuilder signInRouteBuilder = new SignInRouteBuilder();
        signInRouteBuilder.configure();
        assertNotNull(signInRouteBuilder.getRouteCollection().getRoutes());
    }
}
