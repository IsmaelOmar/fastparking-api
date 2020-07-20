package com.fastparking.api.database.hibernate.service;


import com.fastparking.api.database.hibernate.entity.UserEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

public class UserDAOTest {

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UserDAO userDAO;

    private UserEntity userEntity;

    @Before
    public void setUp() {
        userDAO = new UserDAOImpl(entityManager);
        userEntity = new UserEntity();
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        doNothing().when(entityManager).persist(userEntity);
        when(entityManager.merge(userEntity)).thenReturn(userEntity);
    }

    @Test
    public void givenUUIDWhenFindByUserIdIsCalledThenReturnResults() {
        List<UserEntity> userDetailsEntityList = userDAO.findByUserID(UUID.fromString("41e8bcc4-a9d7-11ea-8bdc-5bbd0245aa42"));
        assertNotNull(userDetailsEntityList);
    }

    @Test
    public void givenUserEntityWhenSaveUserIsCalledThenReturnResults() {
        userDAO.saveUser(userEntity);
    }
}
