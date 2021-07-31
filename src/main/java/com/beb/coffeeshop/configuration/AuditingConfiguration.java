package com.beb.coffeeshop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables entity auditions
 * 
 * @author Beyazit
 * @category Configuration
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {

}
