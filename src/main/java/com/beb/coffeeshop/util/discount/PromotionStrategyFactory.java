package com.beb.coffeeshop.util.discount;

import java.math.BigDecimal;

import com.beb.coffeeshop.model.Order;

/**
 * Gets the promotion strategy which is applicable for the order.
 * 
 * @author Beyazit
 * @category Util
 */
public class PromotionStrategyFactory {

    // Default configurations
    protected static BigDecimal cartLimitForPercentagePromotionStrategy = BigDecimal.valueOf(12.0);
    protected static int minimumDrinkLimitForFreeProductPromotionStrategy = 3;

    private PercentagePromotionStrategy percentagePromotionStrategy;
    private FreeProductPromotionStrategy freeProductPromotionStrategy;

    /**
     * 
     * @param order
     * @return Promotion
     * 
     *         Null case left unhandled deliberately to avoid creating Discount
     *         entity record in case of no discount case.
     */
    public PromotionStrategy getPromotion(Order order) {
        percentagePromotionStrategy = new PercentagePromotionStrategy(order, cartLimitForPercentagePromotionStrategy);
        freeProductPromotionStrategy = new FreeProductPromotionStrategy(order,
                minimumDrinkLimitForFreeProductPromotionStrategy);

        boolean percentagePromotionStrategyIsApplicable = percentagePromotionStrategy.isApplicable();
        boolean freeProductPromotionStrategyIsApplicable = freeProductPromotionStrategy.isApplicable();

        if (freeProductPromotionStrategyIsApplicable && percentagePromotionStrategyIsApplicable)
            return getPromotionWithMaximumDiscount();
        else if (freeProductPromotionStrategyIsApplicable)
            return freeProductPromotionStrategy;
        else if (percentagePromotionStrategyIsApplicable)
            return percentagePromotionStrategy;
        else
            return null;

    }

    /**
     * Operates if the two discount strategy intercests
     * 
     * @return PromotionStrategy with maximum dicount
     */
    private PromotionStrategy getPromotionWithMaximumDiscount() {
        BigDecimal freeProductPromotionStrategyDiscount = freeProductPromotionStrategy.getDiscountAmount();
        BigDecimal percentagePromotionStrategyDiscout = percentagePromotionStrategy.getDiscountAmount();
        if (percentagePromotionStrategyDiscout.compareTo(freeProductPromotionStrategyDiscount) > 0)
            return percentagePromotionStrategy;
        else
            return freeProductPromotionStrategy;

    }

}
