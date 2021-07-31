package com.beb.coffeeshop.service;

import java.util.List;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.OrderStates;
import com.beb.coffeeshop.model.projection.OrderAmountByUserProjection;
import com.beb.coffeeshop.model.projection.ToppingBeverageDistributionProjection;

/**
 * Service for statistics operations
 * 
 * @author Beyazit
 * @category Service
 */
public interface StatisticsService {

    /**
     * 
     * @param orderState
     * @return ToppingBeverageDistributionProjection
     * @throws ServiceException
     */
    List<ToppingBeverageDistributionProjection> getToppingBeverageDistribution(OrderStates orderState)
            throws ServiceException;

    /**
     * 
     * @param orderState
     * @return OrderAmountByUserProjection
     * @throws ServiceException
     */
    List<OrderAmountByUserProjection> getOrderAmountsByUser(OrderStates orderState) throws ServiceException;
}
