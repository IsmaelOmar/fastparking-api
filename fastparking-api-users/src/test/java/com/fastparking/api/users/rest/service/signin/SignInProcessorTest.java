package com.fastparking.api.users.rest.service.signin;

import com.fastparking.api.database.hibernate.entity.UserLoginEntity;
import com.fastparking.api.database.hibernate.service.UserLoginDAO;
import com.fastparking.api.database.hibernate.service.UserLoginDAOImpl;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fastparking.api.users.rest.service.TestData.buildTestUserLoginEntityObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SignInProcessorTest {

    private Exchange exchange;
    private SignInProcessor signInProcessor;
    private SignIn signIn;
    private UserLoginDAO mockedUserLoginDAO;

    @BeforeEach
    public void setUp() {
        CamelContext ctx = new DefaultCamelContext();
        exchange = new DefaultExchange(ctx);
        signInProcessor = new SignInProcessor();
        signIn = new SignIn();
        mockedUserLoginDAO = Mockito.mock(UserLoginDAOImpl.class);
    }

    @Test
    public void givenNoRequestBodyWhenProcessorIsCalledExpectException() {
        signInProcessor.setSignIn(signIn);
        assertThrows(FastParkingApplicationException.class, () -> {
           signInProcessor.process(exchange);
        });
    }

    @Test
    public void givenValidRequestBodyWhenProcessorIsCalledExpectNoException() throws IOException {
        signInProcessor.setSignIn(signIn);
        List<UserLoginEntity> userLoginEntityList = new ArrayList<>();
        UserLoginEntity userLoginEntity = new UserLoginEntity();
        userLoginEntity.setUsername("MyNameIsJeff");
        userLoginEntity.setPassword("MyNameIsJeff");
        userLoginEntity.setSignedIn(false);
        userLoginEntity.setUserLoginId(UUID.randomUUID());
        userLoginEntityList.add(userLoginEntity);

        buildTestUserLoginEntityObject(exchange, userLoginEntity);

        when(mockedUserLoginDAO.findByUsernameAndPassword("MyNameIsJeff", "MyNameIsJeff")).thenReturn(userLoginEntityList);

        userLoginEntity.setSignedIn(true);
        doNothing().when(mockedUserLoginDAO).saveUserLoginDetails(userLoginEntity);
        UserLoginEntity insUserLoginEntity = exchange.getIn().getBody(UserLoginEntity.class);

        assertEquals(insUserLoginEntity, userLoginEntity);

    }
}
