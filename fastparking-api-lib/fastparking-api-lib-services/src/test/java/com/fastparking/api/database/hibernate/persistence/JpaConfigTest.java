package com.fastparking.api.database.hibernate.persistence;




import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManagerFactory;
import java.beans.PropertyVetoException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JpaConfigTest {

    @Mock
    private Environment env;

    @Mock
    private EntityManagerFactory emf;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private JpaConfig jpaConfig;

    @Before
    public void setUp() {
        jpaConfig = new JpaConfig();
        ReflectionTestUtils.setField(jpaConfig, "env", env);
    }

    @Test
    public void testEntityManagerFactory() throws PropertyVetoException {
        when(env.getProperty(any(String.class))).thenReturn("20");
        assertNotNull(jpaConfig.entityManagerFactory());
    }

    @Test
    public void testExceptionTranslation(){
        assertNotNull(jpaConfig.exceptionTranslation());
    }

    @Test
    public void testTransactionManager(){
        assertNotNull(jpaConfig.transactionManager(emf));
    }
}
