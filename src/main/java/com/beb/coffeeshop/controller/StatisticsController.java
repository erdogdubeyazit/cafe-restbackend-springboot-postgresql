package com.beb.coffeeshop.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.OrderStates;
import com.beb.coffeeshop.model.projection.OrderAmountByUserProjection;
import com.beb.coffeeshop.model.projection.ToppingBeverageDistributionProjection;
import com.beb.coffeeshop.presentation.result.admin.OrderAmountByUserProjectionResult;
import com.beb.coffeeshop.presentation.result.admin.ToppingBeverageDistributionStatisticsResult;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;
import com.beb.coffeeshop.security.ApiUserDetails;
import com.beb.coffeeshop.security.CurrentUser;
import com.beb.coffeeshop.service.StatisticsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles report generation requests
 * 
 * @author Beyazit
 * @category Controller
 */
@RestController
@RequestMapping("/api/statistics/")
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    /**
     * Most used toppings for drinks.
     * 
     * @param currentUser authenticated user
     * @return ToppingBeverageDistributionStatisticsResult
     */
    @GetMapping(path = "most-used-toppings-for-drinks")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> getToppingBeverageDistribution(@CurrentUser ApiUserDetails currentUser) {

        try {
            List<ToppingBeverageDistributionProjection> data = statisticsService
                    .getToppingBeverageDistribution(OrderStates.COMPLETED);
            return ToppingBeverageDistributionStatisticsResult.build(data);
        } catch (ServiceException e) {
            logger.error("`StatisticsService.statisticsService()` failed", e);
            return Result.failure("Error while generating statistics");
        }

    }

    /**
     * Total amount of the orders per customer.
     * 
     * @param currentUser authenticated user
     * @return OrderAmountByUserProjectionResult
     */
    @GetMapping(path = "total-orderamount-per-customer")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> getOrderAmountsByUser(@CurrentUser ApiUserDetails currentUser) {

        try {
            List<OrderAmountByUserProjection> data = statisticsService.getOrderAmountsByUser(OrderStates.COMPLETED);
            return OrderAmountByUserProjectionResult.build(data);
        } catch (ServiceException e) {
            logger.error("`StatisticsService.statisticsService()` failed", e);
            return Result.failure("Error while generating statistics");
        }

    }

}
