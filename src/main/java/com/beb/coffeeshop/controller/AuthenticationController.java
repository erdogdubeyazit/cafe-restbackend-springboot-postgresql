package com.beb.coffeeshop.controller;

import javax.validation.Valid;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.exception.UsernameExistsException;
import com.beb.coffeeshop.model.Role;
import com.beb.coffeeshop.presentation.payload.authentication.LoginPayload;
import com.beb.coffeeshop.presentation.payload.authentication.RegisterPayload;
import com.beb.coffeeshop.presentation.result.authentication.AuthenticationResult;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;
import com.beb.coffeeshop.security.JwtTokenProvider;
import com.beb.coffeeshop.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller for login and signup operations.
 * 
 * @implNote Relies on JWT
 * @author Beyazit
 * @category Controller
 */
@RestController
@RequestMapping("/auth/")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    /**
     * Handles login requests
     * 
     * @param payload : {username:'', password:''}
     * @return JWT in bearer token
     */
    @PostMapping(path = "signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginPayload payload) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new AuthenticationResult(accessToken));
        } catch (BadCredentialsException e) {
            ApiResult result = ApiResult.error("Bad credentials", "Login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }

    /**
     * Handles signup requests
     * 
     * @param payload : {username:'', password:''}
     */
    @PostMapping(path = "signup")
    public ResponseEntity<?> createUserWithRoleUser(@Valid @RequestBody RegisterPayload payload) {
        try {
            userService.create(payload.getUsername(), payload.getPassword(), Role.ROLE_USER);
            return Result.created();
        } catch (UsernameExistsException e) {
            return Result.failure("Username already exists");
        } catch (ServiceException e) {
            logger.error("Registration failed", e);
            return Result.failure("Registration failed");
        }
    }

}
