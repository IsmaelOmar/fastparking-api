package com.fastparking.api.users.rest.service.userregistration;

import com.fastparking.api.database.hibernate.entity.UserEntity;
import com.fastparking.api.database.hibernate.entity.UserLoginEntity;
import com.fastparking.api.database.hibernate.service.UserDAO;
import com.fastparking.api.database.hibernate.service.UserLoginDAO;
import com.fastparking.api.lib.commons.encryption.EncryptionUtil;
import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import com.fastparking.api.schema.UserRegistrationRequest;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static com.fastparking.api.lib.commons.constants.DataConstants.ORIGINAL_REQUEST;
import static com.fastparking.api.lib.commons.util.FastParkingUtil.formatCurrentDateToString;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class UserRegistration {

    public static final Logger LOGGER = getLogger(UserRegistration.class);

    @Autowired
    private UserLoginDAO userLoginDAO;

    @Autowired
    private UserDAO userDAO;


    public void createNewUser(Exchange exchange) {
        UserRegistrationRequest userRegistrationRequest = exchange.getIn().getBody(UserRegistrationRequest.class);

        if (userRegistrationRequest != null) {

            exchange.getIn().getHeaders().put(ORIGINAL_REQUEST, userRegistrationRequest);

            EncryptionUtil encryptionUtil = new EncryptionUtil();

            String encrpytedPassword = null;

            try {
                encrpytedPassword = encryptionUtil.encrypt(userRegistrationRequest.getUserLogin().getPassword());
            } catch (FastParkingApplicationException e) {
                LOGGER.error("Unable to encrypt password from user registration request : " + e.getMessage());
            }

            UserLoginEntity userLoginEntity = new UserLoginEntity();
            userLoginEntity.setUsername(userRegistrationRequest.getUserLogin().getUsername());
            userLoginEntity.setPassword(encrpytedPassword);
            userLoginEntity.setUserLoginId(UUID.randomUUID());
            userLoginEntity.setSignedIn(false);

            LOGGER.info("Starting Call to UserLoginDAO.saveUserLoginDetails");
            userLoginDAO.saveUserLoginDetails(userLoginEntity);
            LOGGER.info("End of Call to UserLoginDAO.saveUserLoginDetails");

            LOGGER.info("Start of Call to UserLoginDAO.findByUsernameAndPassword");
            List<UserLoginEntity> userLoginEntities = userLoginDAO.findByUsernameAndPassword(userLoginEntity.getUsername(), userLoginEntity.getPassword());
            LOGGER.info("End of call to UserLoginDAO.findByUsernameAndPassword");

            UUID userLoginId = null;

            if(!userLoginEntities.isEmpty()) {
                userLoginEntity = userLoginEntities.get(0);
                userLoginId = userLoginEntity.getUserLoginId();
            } else {
                LOGGER.error("UserLoginId for newly created user not found");
            }

            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(userRegistrationRequest.getUserDetails().getEmail());
            userEntity.setFirstName(userRegistrationRequest.getUserDetails().getFirstName());
            userEntity.setLastName(userRegistrationRequest.getUserDetails().getLastName());
            userEntity.setLicensePlate(userRegistrationRequest.getUserDetails().getLicensePlate());
            userEntity.setUserLoginId(userLoginId);
            userEntity.setUserId(UUID.randomUUID());
            userEntity.setDateOfSignUp(Date.valueOf(formatCurrentDateToString()));

            LOGGER.info("Start of UserDAO.saveUser");
            userDAO.saveUser(userEntity);
            LOGGER.info("End of UserDAO.saveUser");

        } else {
            LOGGER.error("User Registration Request cannot be null:  ");
            throw new IllegalArgumentException("User Registration Request cannot be null");
        }
    }

    public void setUserLoginDAO(UserLoginDAO userLoginDAO) {
        this.userLoginDAO = userLoginDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
