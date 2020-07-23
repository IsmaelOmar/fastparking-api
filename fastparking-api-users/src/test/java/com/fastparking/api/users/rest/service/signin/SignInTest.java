package com.fastparking.api.users.rest.service.signin;

import com.fastparking.api.database.hibernate.entity.UserLoginEntity;
import com.fastparking.api.database.hibernate.service.UserLoginDAO;
import com.fastparking.api.database.hibernate.service.UserLoginDAOImpl;
import com.fastparking.api.lib.commons.encryption.EncryptionUtil;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.schema.UserSignInRequest;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fastparking.api.lib.commons.constants.DataConstants.SIGN_IN_RESULT;
import static com.fastparking.api.lib.commons.constants.DataConstants.TECHNICAL_ERROR_CD;
import static com.fastparking.api.users.rest.service.TestData.buildTestUserSignInRequestObject;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.powermock.api.easymock.PowerMock.createMockAndExpectNew;


public class SignInTest {

    private Exchange exchange;
    private UserLoginDAO userLoginDAO;
    private SignIn signIn;

    @Before
    @BeforeEach
    public void setUp() {
        CamelContext ctx = new DefaultCamelContext();
        exchange = new DefaultExchange(ctx);
        userLoginDAO = mock(UserLoginDAOImpl.class);
        signIn = new SignIn();
    }

    @Test
    public void givenExchangeBodyIsNullWhenCheckUserCredentialsForSignInIsCalledExpectFastParkingApplicationException() {
        exchange.getIn().setBody(null);
        signIn.setUserLoginDAO(userLoginDAO);
        assertThrows(FastParkingApplicationException.class, () -> {
            signIn.checkUserCredentialsForSignIn(exchange);
        });
    }

    @Test
    public void givenEncryptedPasswordCannotBeEncrytedWhenCheckUserCredentialsForSignInIsCalledExpectFastParkingApplicationException() throws Exception {
        buildTestUserSignInRequestObject(exchange, false);
        signIn.setUserLoginDAO(userLoginDAO);
        assertThrows(FastParkingApplicationException.class, () -> {
           signIn.checkUserCredentialsForSignIn(exchange);
        });
    }

    @Test
    public void givenUserLoginEntityFoundWhenCheckUserCredentialsForSignInIsCalledExpectNoErrors() throws FastParkingApplicationException {
        buildTestUserSignInRequestObject(exchange, true);
        signIn.setUserLoginDAO(userLoginDAO);

        List<UserLoginEntity> userLoginEntities = new ArrayList<>();
        UserLoginEntity userLoginEntity = new UserLoginEntity();
        userLoginEntity.setSignedIn(false);
        userLoginEntity.setUserLoginId(UUID.randomUUID());
        userLoginEntity.setUsername("MyNameIsJeff");
        userLoginEntity.setPassword("BMYj+w2lTLuALqGaiWH/dA==");
        userLoginEntities.add(userLoginEntity);

        when(userLoginDAO.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(userLoginEntities);
        doAnswer((invocationOnMock) -> {
            userLoginEntity.setSignedIn(true);
            return null;
        }).when(userLoginDAO).saveUserLoginDetails(userLoginEntity);

        signIn.checkUserCredentialsForSignIn(exchange);

        UserLoginEntity userLoginEntity1 = exchange.getIn().getBody(UserLoginEntity.class);

        assertNotNull(userLoginEntity1);
        assertEquals(exchange.getIn().getHeaders().get(SIGN_IN_RESULT), "Signed In Successfully");

    }

    @Test
    public void givenUserLoginEntityIsNotFoundWhenCheckUserCredentialsForSignInIsCalledExpectEmptyExchangeBody() throws FastParkingApplicationException {
        buildTestUserSignInRequestObject(exchange, true);
        signIn.setUserLoginDAO(userLoginDAO);

        List<UserLoginEntity> userLoginEntities = new ArrayList<>();

        when(userLoginDAO.findByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(userLoginEntities);

        signIn.checkUserCredentialsForSignIn(exchange);
        assertEquals(exchange.getIn().getHeaders().get(SIGN_IN_RESULT), "Unsuccessful Sign In, Please check if the Username and Password are correct");

    }



}
