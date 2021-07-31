package com.beb.coffeeshop.configuration;

import java.util.Optional;

import com.beb.coffeeshop.security.ApiUserDetails;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Configures entity auditor
 * 
 * @author Beyazit
 * @category Configuration
 */
@Component
public class CustomAuditingAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && (auth instanceof AnonymousAuthenticationToken == false))
            return Optional.ofNullable(((ApiUserDetails) auth.getPrincipal()).getUserId());
        else
            return Optional.empty();
    }

}
