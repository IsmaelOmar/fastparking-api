package com.fastparking.api.lib.builder.responsebuilder;

import com.fastparking.api.schema.User;
import com.fastparking.api.schema.UserLogin;
import com.fastparking.api.schema.UserRegistrationRequest;
import com.fastparking.api.schema.UserRegistrationResponse;
import com.fastparking.api.schema.UserSignInRequest;
import com.fastparking.api.schema.UserSignInResponse;
import com.fastparking.api.schema.Error;
import org.apache.camel.Exchange;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static com.fastparking.api.lib.builder.responsebuilder.BuilderTransformations.getKeyFromJson;
import static com.fastparking.api.lib.commons.constants.DataConstants.*;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ResponseBuilderImpl implements ResponseBuilder{

    public static final Logger LOGGER = getLogger(ResponseBuilderImpl.class);

    @Override
    public UserRegistrationResponse buildUserRegistrationResponse(Exchange exchange) {
        UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
        UserRegistrationRequest userRegistrationRequest = (UserRegistrationRequest) exchange.getIn().getHeader(ORIGINAL_REQUEST);

        String username = userRegistrationRequest.getUserLogin().getUsername();

        userRegistrationResponse.setResult("User " + username + " created successfully");
        LOGGER.info("UserRegistration response built successfully");

        return userRegistrationResponse;
    }

    @Override
    public UserSignInResponse buildUserSignInResponse(Exchange exchange) {
        UserSignInResponse userSignInResponse = new UserSignInResponse();
        String message = (String) exchange.getIn().getHeader(SIGN_IN_RESULT);
        userSignInResponse.setResult(message);

        return userSignInResponse;
    }

    @Override
    public Error buildErrorResponse(Exchange exchange) {

        Error error = new Error();

        String errorCode = (String) exchange.getIn().getHeader(ERROR_CODE);
        String errorMessage = (String) exchange.getIn().getHeader(ERROR_MESSAGE);

        error.setCode(errorCode);
        error.setMessage(errorMessage);

        return error;
    }

    @Override
    public Error buildSignInErrorResponseForValidationException(Exchange exchange) {
        return null;
    }

    @Override
    public Error buildUserRegistrationErrorResponseForValidationException(Exchange exchange) {
        return null;
    }

//    @Override
//    public Error buildSignInErrorResponseForValidationException(Exchange exchange) {
//        String requestBody = exchange.getIn().getHeader(ORIGINAL_REQUEST).toString();
//        UserSignInRequest userSignInRequest = new UserSignInRequest();
//
//        try {
//            JSONObject userSignInRequestJsonObj = new JSONObject(requestBody);
//
//            if (getKeyFromJson(userSignInRequestJsonObj, "username") != null) {
//                userSignInRequest.setUsername(userSignInRequestJsonObj.getString("username"));
//            }
//            if (getKeyFromJson(userSignInRequestJsonObj, "password") != null) {
//                userSignInRequest.setPassword(userSignInRequestJsonObj.getString("password"));
//            }
//        } catch (JSONException e) {
//            LOGGER.error("Exception while creating new JSON Object from UserSignInRequest: ", e);
//        }
//        Error error = new Error();
//        String errorCode = (String) exchange.getIn().getHeader(ERROR_CODE);
//        String errorMessage = (String) exchange.getIn().getHeader(ERROR_MESSAGE);
//
//        error.setMessage(errorMessage);
//        error.setCode(errorCode);
//        return error;
//    }
//
//    @Override
//    public Error buildUserRegistrationErrorResponseForValidationException(Exchange exchange) {
//        String requestBody = exchange.getIn().getHeader(ORIGINAL_REQUEST).toString();
//        UserLogin userLogin = new UserLogin();
//        User user = new User();
//        try {
//            JSONObject jsonObject = new JSONObject(requestBody);
//            JSONObject userLoginJsonObj = jsonObject.getJSONObject("userLogin");
//
//            if (getKeyFromJson(userLoginJsonObj, "username") != null) {
//                userLogin.setUsername(userLoginJsonObj.getString("username"));
//            }
//            if (getKeyFromJson(userLoginJsonObj, "password") != null) {
//                userLogin.setPassword(userLoginJsonObj.getString("password"));
//            }
//
//            JSONObject userDetailsJsonObj =  jsonObject.getJSONObject("userDetails");
//
//            if (getKeyFromJson(userDetailsJsonObj, "email") != null) {
//                user.setEmail(userDetailsJsonObj.getString("email"));
//            }
//            if (getKeyFromJson(userDetailsJsonObj, "firstName") != null) {
//                user.setFirstName(userDetailsJsonObj.getString("firstName"));
//            }
//            if (getKeyFromJson(userDetailsJsonObj, "lastName") != null) {
//                user.setLastName(userDetailsJsonObj.getString("lastName"));
//            }
//            if (getKeyFromJson(userDetailsJsonObj, "licensePlate") != null) {
//                user.setLicensePlate(userDetailsJsonObj.getString("licensePlate"));
//            }
//        } catch (JSONException e) {
//            LOGGER.error("Exception while creating new JSON Object from UserRegistrationRequest: ", e);
//        }
//
//        Error error = new Error();
//
//        String errorCode = (String) exchange.getIn().getHeader(ERROR_CODE);
//        String errorMessage = (String) exchange.getIn().getHeader(ERROR_MESSAGE);
//
//        error.setCode(errorCode);
//        error.setMessage(errorMessage);
//
//        return error;
//    }
}
