package com.beb.coffeeshop.repository;

import java.util.Optional;

import com.beb.coffeeshop.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Search user by name
     * 
     * @param username name of the user
     * @return User. Consider result can be null.
     */
    Optional<User> findByUsername(String username);

}
