package com.beb.coffeeshop.service;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.exception.UsernameExistsException;
import com.beb.coffeeshop.model.Role;
import com.beb.coffeeshop.model.User;

/**
 * User service
 * 
 * @author Beyazit
 * @category Service
 */
public interface UserService {
    /**
     * Creates user
     * 
     * @implSpec checks uniqueness of the username
     * @implSpec hashes the password
     * @param userName name of the user
     * @param password password of the user
     * @param role     role of the user
     * @return User
     * @throws UsernameExistsException
     * @throws ServiceException
     */
    User create(String userName, String password, Role role) throws UsernameExistsException, ServiceException;
}
