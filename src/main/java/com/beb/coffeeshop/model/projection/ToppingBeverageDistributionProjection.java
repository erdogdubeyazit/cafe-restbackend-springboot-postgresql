package com.beb.coffeeshop.model.projection;

/**
 * Data structure for ToppingBeverageDistribution statistics operation
 * 
 * @author Beyazit
 * @category Projection
 */
public interface ToppingBeverageDistributionProjection {

    /**
     * 
     * @return name of the topping user for a beverage
     */
    String getToppingName();

    /**
     * 
     * @return beverage name used with a topping
     */
    String getBeverageName();

    /**
     * 
     * @return Beverage and Topping coumbination count
     */
    Long getItemCount();

}
