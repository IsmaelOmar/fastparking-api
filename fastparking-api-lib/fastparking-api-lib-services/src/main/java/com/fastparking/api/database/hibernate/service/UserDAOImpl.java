package com.fastparking.api.database.hibernate.service;

import com.fastparking.api.database.hibernate.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserDAOImpl implements UserDAO<UserEntity, UUID> {

    @PersistenceContext
    private final EntityManager entityManager;

    private static final String JOINFETCH =
            "left outer join fetch u.userLoginEntity " +
            "left outer join fetch u.bookingEntitySet ";

    /**
     * The constructor
     * @param entityManager The hibernate entity manager
     */
    public UserDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<UserEntity> findByUserID(UUID userId) {
        Query query = entityManager.createQuery("from UserEntity u " +
                JOINFETCH +
                "where u.userId=:arg1");
        query.setParameter("arg1", userId.toString());
        return (List<UserEntity>) query.getResultList().stream().distinct().collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveUser(UserEntity userEntity) {
        if (userEntity.getUserId() == null) {
            entityManager.persist(userEntity);
        }
        else {
            entityManager.merge(userEntity);
        }
    }
}
