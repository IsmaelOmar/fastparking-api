package com.fastparking.api.schema.validation.utils;

import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;

import com.fastparking.api.lib.schema.validation.utils.SchemaValidator;
import com.fastparking.api.schema.User;
import com.fastparking.api.schema.UserLogin;
import com.fastparking.api.schema.UserRegistrationRequest;
import org.everit.json.schema.ValidationException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;


import static com.fastparking.api.lib.schema.validation.utils.SchemaValidator.validateAgainstSchema;
import static com.fastparking.api.lib.schema.validation.utils.SchemaValidator.validateUserRegistrationRequestAgainstSchema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Disabled
public class SchemaValidatorTest {

    @Test
    public void givenValidateAgainstSchemaIsCalledWithValidJsonRequestThenExpectNoValidationErrors() throws IOException {
        String schemaFileName = "components.json";
        String jsonRequest = "{" +
                "\"user\" : { " +
                "\"email\" : \"first.last@gmail.com\", " +
                "\"firstName\" : \"Jimmy\"," +
                "\"lastName\" : \"Fallon\"," +
                "\"password\" : \"GenDen123\"," +
                "\"username\" : \"JFallon\"," +
                "\"licensePlate\" : \"TV15 RT6\"" +
                " }" +
                " }";
        validateAgainstSchema(schemaFileName, jsonRequest, "userRegistrationRequest");
    }

    @Test
    public void givenValidateAgainstSchemaIsCalledWithInValidJsonRequestThenExpectValidationErrors() throws IOException {
        String schemaFileName = "components.json";
        String jsonRequest = "{" +
                "\"user\" : { " +
                "\"email\" : \"first.last@gmail.com\", " +
                "\"firstName\" : \"Jimmy\"," +
                "\"lastName\" : \"Fallon\"," +
                "\"password\" : \"GenDen123\"," +
                "\"username\" : \"JFallon\"" +
                " }" +
                " }";
        assertThrows(ValidationException.class, () -> {
            validateAgainstSchema(schemaFileName, jsonRequest, "userRegistrationRequest");
        } );
    }

    @Test
    public void givenValidateUserRegistrationRequestAgainstSchemaIsCalledWithValidUserRegistrationRequestObjectThenExpectNoValidationErrors() throws FastParkingApplicationException {
        String schemaFileName = "components.json";

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        User user = new User();
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername("JFallon");
        user.setEmail("jimmy.fallon@gmail.com");
        userLogin.setPassword("DummyPassword");
        user.setFirstName("Jimmy");
        user.setLastName("Fallon");
        user.setLicensePlate("TV14 RQ1");

        userRegistrationRequest.setUserDetails(user);
        userRegistrationRequest.setUserLogin(userLogin);

        validateUserRegistrationRequestAgainstSchema(schemaFileName, userRegistrationRequest);
    }

    @Test
    public void givenValidateUserRegistrationRequestAgainstSchemaIsCalledWithInValidUserRegistrationRequestObjectThenExpectValidationErrors() throws FastParkingApplicationException {
        String schemaFileName = "components.json";
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();

        assertThrows(FastParkingApplicationException.class, () -> {
            validateUserRegistrationRequestAgainstSchema(schemaFileName, userRegistrationRequest);
        });
    }

    @Test
    public void testClassDefinition() {
        assertTrue("Class must be final",
                Modifier.isFinal(SchemaValidator.class.getModifiers()));
        assertEquals("There must be only one constructor", 1,
                SchemaValidator.class.getDeclaredConstructors().length);
    }

    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException {
        Constructor<SchemaValidator> constructor = SchemaValidator.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
    }


}
