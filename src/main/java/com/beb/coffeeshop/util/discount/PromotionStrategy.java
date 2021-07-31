package com.beb.coffeeshop.util.discount;

import java.math.BigDecimal;


/**
 * @author Beyazit
 * @category Util
 */
public interface PromotionStrategy {

    /**
     * 
     * @return Discount information
     * @throws PromotionStrageyException
     */
    public String getDicsountInfo();

    /**
     * 
     * @return calculated total discount depending on the discount logic.
     * @throws PromotionStrageyException
     */
    public BigDecimal getDiscountAmount();

}
