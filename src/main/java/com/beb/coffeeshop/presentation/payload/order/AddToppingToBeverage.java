package com.beb.coffeeshop.presentation.payload.order;

import javax.validation.constraints.NotBlank;

public class AddToppingToBeverage {

    @NotBlank
    private String toppingName;

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }

    @Override
    public String toString() {
        return "AddToppingToBeverage [toppingName=" + toppingName + "]";
    }

}
