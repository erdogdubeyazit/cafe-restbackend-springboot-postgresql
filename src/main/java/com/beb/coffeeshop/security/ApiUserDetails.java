package com.beb.coffeeshop.security;

import java.util.Collection;
import java.util.Collections;

import com.beb.coffeeshop.model.Role;
import com.beb.coffeeshop.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User details for api operations.
 */
public class ApiUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Role role;

    private ApiUserDetails(Long userId, String username, String password, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Creates ApiUSerDetails instance from user
     * 
     * @param user
     * @return ApiUserDetails
     */
    public static ApiUserDetails create(User user) {
        return new ApiUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

    public long getUserId() {
        return userId;
    }

    /**
     * @return roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Consider only user name
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApiUserDetails other = (ApiUserDetails) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
