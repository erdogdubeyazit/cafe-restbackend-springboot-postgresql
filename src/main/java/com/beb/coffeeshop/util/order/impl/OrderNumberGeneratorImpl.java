package com.beb.coffeeshop.util.order.impl;

import java.util.UUID;

import com.beb.coffeeshop.util.order.OrderNumberGenerator;

/**
 * Default order number generator
 */
public class OrderNumberGeneratorImpl implements OrderNumberGenerator {

    @Override
    public String generate() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }

}
