package com.fastparking.api.database.hibernate.service;

import com.fastparking.api.database.hibernate.entity.UserLoginEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserLoginDAOImpl implements UserLoginDAO<UserLoginEntity, UUID> {

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * The constructor
     * @param entityManager The hibernate entity manager
     */
    public UserLoginDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<UserLoginEntity> findByUsernameAndPassword(String username, String password) {
        Query query = entityManager.createQuery("from UserLoginEntity ule " +
                "where ule.username=:arg1  " +
                "and ule.password=:arg2");
        query.setParameter("arg1", username);
        query.setParameter("arg2", password);
        return (List<UserLoginEntity>) query.getResultList().stream().distinct().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveUserLoginDetails(UserLoginEntity userLoginEntity) {
        if (userLoginEntity.getUserLoginId() == null) {
           entityManager.persist(userLoginEntity);
        }
        else {
            entityManager.merge(userLoginEntity);
        }
    }
}
