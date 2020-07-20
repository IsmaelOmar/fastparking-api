package com.fastparking.api.database.hibernate.service;

import java.io.Serializable;
import java.util.List;

/**
 * The DAO interface for retrieving, creating and updating a
 * user and their details
 *
 * @param <T> the entity object
 * @param <I> is the I of the entity object
 */
public interface UserDAO<T, I extends Serializable> {

    /**
     * The method is responsible for getting all the details of a user
     * based on user id
     * @param i the userId of the user entity
     * @return
     */
    List<T> findByUserID(I i);

    /**
     *  The method is responsible for saving a new user or
     *  updating an existing user in the database
     * @param t the user entity to be saved
     * @return
     */
    void saveUser(T t);
}
