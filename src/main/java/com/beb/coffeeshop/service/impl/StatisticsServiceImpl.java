package com.beb.coffeeshop.service.impl;

import java.util.List;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.OrderStates;
import com.beb.coffeeshop.model.projection.OrderAmountByUserProjection;
import com.beb.coffeeshop.model.projection.ToppingBeverageDistributionProjection;
import com.beb.coffeeshop.repository.statistic.OrderItemStatisticsRepository;
import com.beb.coffeeshop.service.StatisticsService;

import io.jsonwebtoken.lang.Assert;

public class StatisticsServiceImpl implements StatisticsService {

    private OrderItemStatisticsRepository orderItemStatisticsRepository;

    public StatisticsServiceImpl(OrderItemStatisticsRepository orderItemStatisticsRepository) {
        this.orderItemStatisticsRepository = orderItemStatisticsRepository;
    }

    @Override
    public List<ToppingBeverageDistributionProjection> getToppingBeverageDistribution(OrderStates orderState)
            throws ServiceException {
        Assert.notNull(orderState, "orderState can not be null");
        try {
            return orderItemStatisticsRepository.getToppingBeverageDistribution(orderState);
        } catch (Exception e) {
            throw new ServiceException("Error occured while retrieving statistics", e);
        }
    }

    @Override
    public List<OrderAmountByUserProjection> getOrderAmountsByUser(OrderStates orderState) throws ServiceException {
        Assert.notNull(orderState, "orderState can not be null");
        try {
            return orderItemStatisticsRepository.getOrderAmountsByUser(orderState);
        } catch (Exception e) {
            throw new ServiceException("Error occured while retrieving statistics", e);
        }
    }

}
