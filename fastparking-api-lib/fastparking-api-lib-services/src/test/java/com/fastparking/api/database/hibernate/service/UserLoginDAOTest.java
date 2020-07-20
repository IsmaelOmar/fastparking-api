package com.fastparking.api.database.hibernate.service;

import com.fastparking.api.database.hibernate.entity.UserLoginEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserLoginDAOTest {

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UserLoginDAO userLoginDAO;

    private UserLoginEntity userLoginEntity;

    @Before
    public void setUp() {
        userLoginDAO = new UserLoginDAOImpl(entityManager);
        userLoginEntity = new UserLoginEntity();
        when(entityManager.createQuery(any(String.class))).thenReturn(query);
        doNothing().when(entityManager).persist(userLoginEntity);
        when(entityManager.merge(userLoginEntity)).thenReturn(userLoginEntity);
    }

    @Test
    public void givenUsernameAndPasswordWhenFindByUsernameAndPasswordIsCalledThenReturnResults() {
        List<UserLoginEntity> userLoginEntityList = userLoginDAO.findByUsernameAndPassword("JFallon", "dummyPassword");
        assertNotNull(userLoginEntityList);
    }

    @Test
    public void givenUserLoginEntityWhenSaveUserLoginDetailsIsCalledThenReturnResults() {
        userLoginDAO.saveUserLoginDetails(userLoginEntity);
    }
}
