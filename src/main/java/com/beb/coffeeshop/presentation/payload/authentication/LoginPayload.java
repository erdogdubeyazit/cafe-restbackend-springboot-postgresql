package com.beb.coffeeshop.presentation.payload.authentication;

import javax.validation.constraints.NotBlank;

public class LoginPayload {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginPayload [password=" + password + ", username=" + username + "]";
    }

}
