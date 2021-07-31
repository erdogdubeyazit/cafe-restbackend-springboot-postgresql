package com.beb.coffeeshop.util.discount;

import java.math.BigDecimal;

import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.model.Order;
import com.beb.coffeeshop.model.Topping;

/**
 * Operates when the cart items count exceeds minimum number of drinks
 * 
 * @implNote minDrinks is comfigurable
 * @author Beyazit
 * @category Util
 */
public class FreeProductPromotionStrategy extends AbstractPromotionStrategy {

    protected int minDrinks = 3;
    private String cheapestProductInfo;
    private BigDecimal discountAmount;
    private boolean cheapestProductEligiblityCheck = true;

    public FreeProductPromotionStrategy(Order order) {
        super(order);

        Beverage cheapestBeverage = getCheapestBeverage();
        Topping cheapestTopping = getCheapestTopping();

        if (cheapestBeverage != null) {
            if (cheapestTopping != null && cheapestBeverage.getPrice().compareTo(cheapestTopping.getPrice()) > 0) {
                cheapestProductInfo = String.format("Min %d drinks limit fulfilled. Topping %s is free", minDrinks,
                        cheapestTopping.getName());
                discountAmount = cheapestTopping.getPrice();
            } else {
                cheapestProductInfo = String.format("Min %d drinks limit fulfilled. Beverage %s is free", minDrinks,
                        cheapestBeverage.getName());
                discountAmount = cheapestBeverage.getPrice();
            }
        } else if (cheapestTopping != null) {
            cheapestProductInfo = String.format("Min %d drinks limit fulfilled. Topping %s is free", minDrinks,
                    cheapestTopping.getName());
            discountAmount = cheapestTopping.getPrice();
        } else {
            cheapestProductEligiblityCheck = false;
        }

    }

    public FreeProductPromotionStrategy(Order order, int minDrinks) {
        this(order);
        this.minDrinks = minDrinks;
    }

    @Override
    public String getDicsountInfo() {
        return cheapestProductInfo;
    }

    @Override
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public boolean isApplicable() {
        return cheapestProductEligiblityCheck
                && getOrder().getItems().stream().map(i -> i.getBeverage()).count() >= minDrinks;
    }

    public int getMinDrinks() {
        return minDrinks;
    }

    public void setMinDrinks(int minDrinks) {
        this.minDrinks = minDrinks;
    }

    public String getCheapestProductInfo() {
        return cheapestProductInfo;
    }

    public void setCheapestProductInfo(String cheapestProductInfo) {
        this.cheapestProductInfo = cheapestProductInfo;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

}
