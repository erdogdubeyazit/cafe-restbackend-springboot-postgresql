package com.beb.coffeeshop.exception;

public class OrderAccessDeniedException extends Exception {

    public OrderAccessDeniedException(String message) {
        super(message);
    }

    public OrderAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}
