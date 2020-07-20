package com.fastparking.api.users.rest.service;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.camel.CamelContext;
import org.apache.cxf.bus.spring.SpringBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = "com.fastparking.api")
public class FastParkingUsersApplication {

    @Autowired
    CamelContext camelContext;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringBus springBus() { return new SpringBus();}

    @Bean
    public JacksonJsonProvider jsonProvider() { return new JacksonJsonProvider(); }

    public static void main(String[] args) {
        SpringApplication.run(FastParkingUsersApplication.class, args);
    }
}
