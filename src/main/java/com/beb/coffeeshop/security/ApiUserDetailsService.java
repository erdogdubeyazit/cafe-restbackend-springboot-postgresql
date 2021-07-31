package com.beb.coffeeshop.security;

import com.beb.coffeeshop.model.User;
import com.beb.coffeeshop.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication and authorization operations.
 * 
 * @implNote This service is auto configured because it won't be used in custom
 *           operations
 */
@Service
public class ApiUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(ApiUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return ApiUserDetails.create(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            throw new UsernameNotFoundException(e.getMessage(), e.getCause());
        }
    }

}