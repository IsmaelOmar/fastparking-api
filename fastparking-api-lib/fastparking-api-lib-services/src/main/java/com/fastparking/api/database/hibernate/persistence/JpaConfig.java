package com.fastparking.api.database.hibernate.persistence;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.fastparking.api.database.hibernate.entity")
public class JpaConfig {

    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        //vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.fastparking.api.database.hibernate.entity");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        em.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);

        return em;
    }

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        ComboPooledDataSource dataSource1 = new ComboPooledDataSource("jupiter");
        dataSource1.setForceUseNamedDriverClass(true);
        dataSource1.setTestConnectionOnCheckin(false);
        dataSource1.setDriverClass("org.postgresql.Driver");
        dataSource1.setUser(env.getProperty("spring.datasource.username"));
        dataSource1.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource1.setPassword(env.getProperty("spring.datasource.password"));
        dataSource1.setMinPoolSize(Integer.parseInt(env.getProperty("spring.jpa.properties.hibernate.c3p0.min_size")));
        dataSource1.setMaxPoolSize(Integer.parseInt(env.getProperty("spring.jpa.properties.hibernate.c3p0.max_size")));
        return dataSource1;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.setProperty("hibernate.current_session_context_class", env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
        properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", env.getProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation"));
        properties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.properties.hibernate.show_sql"));
        properties.setProperty("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
        properties.setProperty("hibernate.default_schema", env.getProperty("spring.jpa.properties.hibernate.default_schema"));
        properties.setProperty("hibernate.generate_statistics", env.getProperty("spring.jpa.properties.hibernate.generate_statistics"));
        properties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache"));
        properties.setProperty("hibernate.cache.region.factory_class", env.getProperty("spring.jpa.properties.hibernate.cache.region.factory_class"));
        properties.setProperty("hibernate.javax.cache.provider", env.getProperty("spring.jpa.properties.hibernate.javax.cache.provider"));
        properties.setProperty("hibernate.javax.cache.missing_cache_strategy", env.getProperty("spring.jpa.properties.hibernate.javax.cache.missing_cache_strategy"));
        properties.setProperty("hibernate.javax.cache.uri", env.getProperty("spring.jpa.properties.hibernate.javax.cache.uri"));
        properties.setProperty("hibernate.cache.use_query_cache", env.getProperty("spring.jpa.properties.hibernate.cache.use_query_cache"));
        properties.setProperty("hibernate.query.plan_cache_max_size", env.getProperty("spring.jpa.properties.hibernate.query.plan_cache_max_size"));
        properties.setProperty("spring.jpa.open-in-view", env.getProperty("spring.jpa.open-in-view"));
        properties.setProperty("hibernate.c3p0.timeout","60");
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults","false");
        return properties;
    }
}
