package com.beb.coffeeshop.exception;

public class OrderNumberNotFoundException extends Exception{

    public OrderNumberNotFoundException(String message) {
        super(message);
    }

    public OrderNumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
