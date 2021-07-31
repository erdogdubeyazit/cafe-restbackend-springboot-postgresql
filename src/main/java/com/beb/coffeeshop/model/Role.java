package com.beb.coffeeshop.model;

/**
 * User roles.
 * 
 * @implNote enum instead of seperate entity preferred to reduce database
 *           interaction.
 */
public enum Role {
    ROLE_USER, ROLE_ADMIN
}
