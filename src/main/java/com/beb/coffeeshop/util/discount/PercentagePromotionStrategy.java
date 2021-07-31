package com.beb.coffeeshop.util.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.beb.coffeeshop.model.Order;

/**
 * Operates when total cart amount exceeds the defined limit.
 * 
 * @implNote cartLimit is configurable
 * @implNote discountRate is configurable
 * @author Beyazit
 * @category Util
 */
public class PercentagePromotionStrategy extends AbstractPromotionStrategy {

    protected BigDecimal cartLimit = BigDecimal.valueOf(12.0);
    protected BigDecimal discountRate = BigDecimal.valueOf(0.25);

    public PercentagePromotionStrategy(Order order) {
        super(order);
    }

    public PercentagePromotionStrategy(Order order, BigDecimal cartLimit) {
        this(order);
        this.cartLimit = cartLimit;
    }

    public PercentagePromotionStrategy(Order order, BigDecimal cartLimit, BigDecimal discountRate) {
        this(order, cartLimit);
        this.discountRate = discountRate;
    }

    @Override
    public String getDicsountInfo() {
        return String.format("%.2f cart limit fulfilled. %.2f rate discount applied",
                cartLimit.setScale(2, RoundingMode.DOWN), discountRate.setScale(2, RoundingMode.DOWN));
    }

    @Override
    public BigDecimal getDiscountAmount() {
        return discountRate.multiply((getBeverageTotal().add(getToppingsTotal())));
    }

    @Override
    public boolean isApplicable() {
        return (getBeverageTotal().add(getToppingsTotal())).compareTo(cartLimit) > 0;
    }

    public BigDecimal getCartLimit() {
        return cartLimit;
    }

    public void setCartLimit(BigDecimal cartLimit) {
        this.cartLimit = cartLimit;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

}
