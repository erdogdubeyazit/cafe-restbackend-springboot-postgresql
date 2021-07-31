package com.beb.coffeeshop.presentation.payload.admin;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

public class BeveragePayload {

    @NotBlank
    private String name;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 2, fraction = 2)
    private BigDecimal price;

    @NotBlank
    private String currency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "BeveragePayload [currency=" + currency + ", name=" + name + ", price=" + price + "]";
    }

}
