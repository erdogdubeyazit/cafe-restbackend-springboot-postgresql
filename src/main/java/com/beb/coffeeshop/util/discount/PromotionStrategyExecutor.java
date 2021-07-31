package com.beb.coffeeshop.util.discount;

import com.beb.coffeeshop.exception.PromotionException;
import com.beb.coffeeshop.model.Discount;

/**
 * Promotion Strategy Context
 * 
 * @implNote Takes Discount object in constructor because the context is bound
 *           with the object's state
 * @author Beyazit
 * @category Util
 */
public class PromotionStrategyExecutor {

    private Discount discount;

    public PromotionStrategyExecutor(Discount discount) {
        this.discount = discount;
    }

    /**
     * Executes the strategy
     * 
     * @param promotion
     * @return Discount
     * @throws PromotionException
     */
    public Discount executeStrategy(PromotionStrategy promotion) throws PromotionException {
        try {
            discount.setDiscountInfo(promotion.getDicsountInfo());
            discount.setDiscountAmount(promotion.getDiscountAmount());
            return discount;
        } catch (Exception e) {
            throw new PromotionException("Error executing promotion strategy", e);
        }
    }

}
