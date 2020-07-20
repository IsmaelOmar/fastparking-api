package com.fastparking.api.users.rest.service.userregistration;

import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.lib.commons.util.MessageIdGenerator;
import com.fastparking.api.users.rest.service.common.DBAuthenticationErrorProcessor;
import com.fastparking.api.users.rest.service.common.EntityNotFoundErrorProcessor;
import com.fastparking.api.users.rest.service.common.FastParkingErrorProcessor;
import com.fastparking.api.users.rest.service.common.PersistenceErrorProcessor;
import com.fastparking.api.users.rest.service.common.UserErrorProcessor;
import com.fastparking.api.users.rest.service.common.UserRegistrationValidationErrorProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.everit.json.schema.ValidationException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

import static com.fastparking.api.lib.commons.constants.DataConstants.TRACKING_ID;

@Component
public class UserResgistrationRouteBuilder extends RouteBuilder {

    @Autowired
    private UserRegistrationProcessor userRegistrationProcessor;

    @Autowired
    private UserRegistrationRequestValidationProcessor userRegistrationRequestValidationProcessor;

    @Autowired
    private UserRegistrationResponseProcessor userRegistrationResponseProcessor;

    @Autowired
    private DBAuthenticationErrorProcessor dbAuthenticationErrorProcessor;

    @Autowired
    private EntityNotFoundErrorProcessor entityNotFoundErrorProcessor;

    @Autowired
    private PersistenceErrorProcessor persistenceErrorProcessor;

    @Autowired
    private FastParkingErrorProcessor fastParkingErrorProcessor;

    @Autowired
    private UserErrorProcessor userErrorProcessor;

    @Autowired
    private UserRegistrationValidationErrorProcessor userRegistrationValidationErrorProcessor;


    @Override
    public void configure() throws Exception {

        onException(IllegalArgumentException.class).process(userErrorProcessor).handled(true).logStackTrace(true);
        onException(JDBCConnectionException.class).process(persistenceErrorProcessor).handled(true).logStackTrace(true);
        onException(SQLGrammarException.class).process(userErrorProcessor).handled(true).logStackTrace(true);
        onException(GenericJDBCException.class).process(dbAuthenticationErrorProcessor).handled(true).logStackTrace(true);
        onException(FastParkingApplicationException.class).process(fastParkingErrorProcessor).handled(true).logStackTrace(true);
        onException(ValidationException.class).process(userRegistrationValidationErrorProcessor).handled(true).logStackTrace(true);
        onException(JSONException.class).process(userRegistrationValidationErrorProcessor).handled(true).logStackTrace(true);
        onException(EntityNotFoundException.class).process(entityNotFoundErrorProcessor).handled(true).logStackTrace(true);
        onException(Exception.class).process(userErrorProcessor).handled(true).logStackTrace(true);

        from("direct:userRegistration")
                .routeId("UserRegistrationRouteBuilder")
                .setHeader(TRACKING_ID, MessageIdGenerator::getMessageId)
                .convertBodyTo(String.class)
                .process(userRegistrationRequestValidationProcessor)
                .process(userRegistrationProcessor)
                .process(userRegistrationResponseProcessor);
    }
}
