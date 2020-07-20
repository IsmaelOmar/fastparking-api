package com.fastparking.api.database.hibernate.service;

import java.io.Serializable;
import java.util.List;

/**
 * he DAO interface for retrieving, creating and updating a
 * user's login information
 *
 * @param <T> the entity object
 * @param <I> is the id of the entity object
 */
public interface UserLoginDAO<T, I extends Serializable> {

    /**
     * The method is responsible for finding a user login entry
     * based on a username and password provided
     *
     * @param username the username
     * @param password the password
     * @return List of user logins based on username and password
     */
    List<T> findByUsernameAndPassword(String username, String password);

    /**
     * The method is responsible for creating a new user login entry or
     * updating an existing one
     *
     * @param t the user login entity
     */
    void saveUserLoginDetails(T t);
}
