package com.beb.coffeeshop.presentation.payload.order;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RemoveBeverageFromOrder {

    @NotNull
    private Long orderItemId;
    @NotBlank
    private String beverageName;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getBeverageName() {
        return beverageName;
    }

    public void setBeverageName(String beverageName) {
        this.beverageName = beverageName;
    }

    @Override
    public String toString() {
        return "RemoveBeverageFromOrder [beverageName=" + beverageName + ", orderItemId=" + orderItemId + "]";
    }

}
