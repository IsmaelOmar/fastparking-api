package com.fastparking.api.users.rest.service.userregistration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationProcessor implements Processor {

    @Autowired
    UserRegistration userRegistration;

    @Override
    public void process(Exchange exchange) throws Exception {
        userRegistration.createNewUser(exchange);
    }

    public void setUserRegistration(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }
}
