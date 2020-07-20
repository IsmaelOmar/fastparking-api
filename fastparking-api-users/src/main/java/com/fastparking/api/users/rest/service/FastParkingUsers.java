package com.fastparking.api.users.rest.service;

import com.fastparking.api.users.rest.service.common.DBAuthenticationErrorProcessor;
import com.fastparking.api.users.rest.service.common.EntityNotFoundErrorProcessor;
import com.fastparking.api.users.rest.service.common.FastParkingErrorProcessor;
import com.fastparking.api.users.rest.service.common.PersistenceErrorProcessor;
import com.fastparking.api.users.rest.service.common.UserErrorProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
public class FastParkingUsers extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.off)
                .dataFormatProperty("prettyPrint", "true");

        rest("/users")
                .post("/registration").to("direct:userRegistration")
                .post("/signin").to("direct:userSignIn")
                .consumes(MediaType.APPLICATION_JSON)
                .produces(MediaType.APPLICATION_JSON);

    }
}
