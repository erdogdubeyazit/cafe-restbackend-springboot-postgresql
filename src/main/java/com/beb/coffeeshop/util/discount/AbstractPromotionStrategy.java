package com.beb.coffeeshop.util.discount;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.model.Order;
import com.beb.coffeeshop.model.OrderItem;
import com.beb.coffeeshop.model.Topping;

/**
 * Abstract parent class of the promotion strategies.
 * 
 * @implNote Calculateions are done outside of the anemic classes
 * @author Beyazit
 * @category Util
 */
public abstract class AbstractPromotionStrategy implements PromotionStrategy {

    private Order order;

    public AbstractPromotionStrategy(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 
     * @return sum of beverage prices
     */
    public BigDecimal getBeverageTotal() {
        return order.getItems().stream().map(i -> i.getBeverage().getPrice()).reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b));
    }

    /**
     * @return sum of topping prices
     */
    public BigDecimal getToppingsTotal() {
        return order.getItems().stream().flatMap(i -> i.getToppings().stream()).map(i -> i.getPrice())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    /**
     * @return cheapest beverrage info in the orders
     */
    public Beverage getCheapestBeverage() {
        Optional<OrderItem> minimumItemWithCheapestBeverage = order.getItems().stream()
                .min(Comparator.comparing(i -> i.getBeverage().getPrice()));
        if (!minimumItemWithCheapestBeverage.isPresent())
            return null;
        return minimumItemWithCheapestBeverage.get().getBeverage();
    }

    /**
     * @return cheapest topping info in the orders
     */
    public Topping getCheapestTopping() {
        Optional<Topping> minimumItemWithCheapestTopping = order.getItems().stream()
                .flatMap(i -> i.getToppings().stream()).min(Comparator.comparing(t -> t.getPrice()));
        if (!minimumItemWithCheapestTopping.isPresent())
            return null;
        return minimumItemWithCheapestTopping.get();

    }

    /**
     * Checks wheter current strategy is applicable
     * 
     * @return boolean value of the test
     */
    public abstract boolean isApplicable();

}
