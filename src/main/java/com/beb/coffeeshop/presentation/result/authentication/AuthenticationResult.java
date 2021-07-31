package com.beb.coffeeshop.presentation.result.authentication;

public class AuthenticationResult {
    private String accessToken;
    private String tokenType = "Bearer";

    public AuthenticationResult(String accessToken) {
		this.accessToken = accessToken;
	}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
