package com.beb.coffeeshop.presentation.payload.order;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AddBeverageToOrderPayload {

    @NotEmpty
    private String beverageName;

    @NotNull
    @Min(1)
    private Integer count;

    public String getBeverageName() {
        return beverageName;
    }

    public void setBeverageName(String beverageName) {
        this.beverageName = beverageName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AddBeverageToOrderPayload [beverageName=" + beverageName + ", count=" + count + "]";
    }

}
