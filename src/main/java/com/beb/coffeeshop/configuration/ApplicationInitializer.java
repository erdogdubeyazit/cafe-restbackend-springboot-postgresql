package com.beb.coffeeshop.configuration;

import com.beb.coffeeshop.model.Role;
import com.beb.coffeeshop.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * This configuration file is for initializin the root user
 * 
 * @author Beyazit
 * @category Inintialization
 */
@Component
public class ApplicationInitializer implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Create admin
        userService.create(applicationProperties.getUser(), applicationProperties.getPassword(), Role.ROLE_ADMIN);

    }

}
