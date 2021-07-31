package com.beb.coffeeshop.configuration;

import javax.validation.Valid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class is responsible for injecting application properties with
 * 
 * @author Beyazit
 * @category Configuration type safety.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Valid
public class ApplicationProperties {

    private String jwtSecret;
    private Integer jwtExpirationInMs;
    private String user;
    private String password;

    /**
     * @return JWTSecret: Salting secret for JWT in Application properties
     */
    public String getJwtSecret() {
        return jwtSecret;
    }

    /**
     * @return jwtExpirationInMs: time limit for JWT
     */
    public Integer getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }

    /**
     * Initial admin user for api management
     * 
     * @return User name
     */
    public String getUser() {
        return user;
    }

    /**
     * Initial admin user password for api management
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param jwtSecret info to set
     */
    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    /**
     * @param jwtExpirationInMs time limit to set
     */
    public void setJwtExpirationInMs(Integer jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    /**
     * @param user user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @param password password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
