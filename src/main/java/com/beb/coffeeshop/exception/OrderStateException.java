package com.beb.coffeeshop.exception;

public class OrderStateException extends Exception {

    public OrderStateException(String message) {
        super(message);
    }

    public OrderStateException(String message, Throwable cause) {
        super(message, cause);
    }

}
