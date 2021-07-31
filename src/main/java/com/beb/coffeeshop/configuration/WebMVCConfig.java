package com.beb.coffeeshop.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Fundamental MVC Cors configuration for web requests
 * 
 * @author Beyazit
 * @category Configuration
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(WebMVCConfig.class);

    private final long MAX_AGE_SECS = 3600;

    /**
     * @param registry : CorsRegistry configuration
     */
    public void addCorsMappings(CorsRegistry registry) {
        try {
            registry.addMapping("/**").allowedOrigins("*")
                    .allowedMethods("HEAD", "OPTIONS", "POST", "PUT", "PATCH", "DELETE").maxAge(MAX_AGE_SECS);
        } catch (Exception e) {
            logger.error("CROSS ORIGIN Configuration failed \n" + e.getMessage(), e.getCause());
        }
    }

}