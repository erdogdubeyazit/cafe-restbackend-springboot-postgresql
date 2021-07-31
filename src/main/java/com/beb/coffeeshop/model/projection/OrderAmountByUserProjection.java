package com.beb.coffeeshop.model.projection;

import java.math.BigDecimal;

/**
 * Data structure for OrderAmountByUser statistics operation
 * @author Beyazit
 * @category Projection
 */
public interface OrderAmountByUserProjection {

    /**
     * 
     * @return user name of the order user
     */
    String getUserName();

    /**
     * 
     * @return amount of paid by the user
     */
    BigDecimal getAmount();

}
