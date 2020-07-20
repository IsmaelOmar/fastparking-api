package com.fastparking.api.users.rest.service.signin;

import com.fastparking.api.database.hibernate.entity.UserLoginEntity;
import com.fastparking.api.database.hibernate.service.UserLoginDAO;
import com.fastparking.api.lib.commons.encryption.EncryptionUtil;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.schema.UserSignInRequest;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;

import static com.fastparking.api.lib.commons.constants.DataConstants.*;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class SignIn {

    private static final Logger LOGGER = getLogger(SignIn.class);

    @Autowired
    private UserLoginDAO userLoginDAO;

    public void checkUserCredentialsForSignIn(Exchange exchange) throws FastParkingApplicationException {
        UserSignInRequest userSignInRequest = exchange.getIn().getBody(UserSignInRequest.class);

        if (userSignInRequest != null) {
            exchange.getIn().getHeaders().put(ORIGINAL_REQUEST, userSignInRequest);

            EncryptionUtil encryptionUtil = new EncryptionUtil();
            String encryptedPassword = null;

            try {
                encryptedPassword = encryptionUtil.encrypt(userSignInRequest.getPassword());
            } catch (FastParkingApplicationException e) {
                LOGGER.error("Unable to encrypt password from user sign in request : " + e.getMessage());
            }

            LOGGER.info("Starting call to UserLoginDAO.findByUsernameAndPassword");
            List<UserLoginEntity> userLoginEntities = userLoginDAO.findByUsernameAndPassword(userSignInRequest.getUsername(), encryptedPassword);
            LOGGER.info("End of call to UserLoginDAO.findByUsernameAndPassword");

            if (!userLoginEntities.isEmpty()) {
                LOGGER.info("Found user with matching username and password");
                UserLoginEntity userLoginEntity = userLoginEntities.get(0);
                userLoginEntity.setSignedIn(true);
                LOGGER.info("Start of Update to user login entity via UserLoginDAO - setting 'signed_in' field to true");
                userLoginDAO.saveUserLoginDetails(userLoginEntity);
                LOGGER.info("Start of Update to user login entity via UserLoginDAO - setting 'signed_in' field to true");
                exchange.getIn().setBody(userLoginEntity);
                exchange.getIn().getHeaders().put(SIGN_IN_RESULT, "Signed In Successfully");
            } else {
                LOGGER.warn("No users found with specified username and password");
                exchange.getIn().getHeaders().put(SIGN_IN_RESULT, "Unsuccessful Sign In, Please check if the Username and Password are correct");
            }
        } else {
            LOGGER.error("User Sign In Request cannot be null: ");
            throw new FastParkingApplicationException("User Sign In Request cannot be null", Response.Status.BAD_REQUEST.getStatusCode(), INVALID_REQUEST_ERROR_CD);
        }

    }

    public void setUserLoginDAO(UserLoginDAO userLoginDAO) {
        this.userLoginDAO = userLoginDAO;
    }
}
