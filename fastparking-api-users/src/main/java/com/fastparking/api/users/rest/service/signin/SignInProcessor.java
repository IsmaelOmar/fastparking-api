package com.fastparking.api.users.rest.service.signin;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignInProcessor implements Processor {

    @Autowired
    private SignIn signIn;

    @Override
    public void process(Exchange exchange) throws Exception {
        signIn.checkUserCredentialsForSignIn(exchange);
    }

    public void setSignIn(SignIn signIn) {
        this.signIn = signIn;
    }
}
