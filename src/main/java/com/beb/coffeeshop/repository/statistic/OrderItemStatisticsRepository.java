package com.beb.coffeeshop.repository.statistic;

import java.util.List;

import com.beb.coffeeshop.model.OrderItem;
import com.beb.coffeeshop.model.OrderStates;
import com.beb.coffeeshop.model.projection.OrderAmountByUserProjection;
import com.beb.coffeeshop.model.projection.ToppingBeverageDistributionProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Data repository for statistics queries.
 */
@Repository
public interface OrderItemStatisticsRepository extends JpaRepository<OrderItem, Long> {

    // Ugly but performant
    // TODO consider using Criteria Api or something better
    @Query(value = "SELECT topping.name as toppingName,orderItem.beverage.name AS beverageName, COUNT(orderItem.beverage.name) AS itemCount FROM OrderItem orderItem JOIN orderItem.toppings topping WHERE orderItem.order.orderState=:orderState GROUP BY orderItem.beverage.name, topping.name")
    List<ToppingBeverageDistributionProjection> getToppingBeverageDistribution(OrderStates orderState);

    // Ugly but performant
    // TODO consider using Criteria Api or something better
    @Query(value = "SELECT ordr.user.username AS userName, SUM(ordr.orderTotal) AS amount FROM Order ordr WHERE ordr.orderState=:orderState GROUP BY ordr.user.username ")
    List<OrderAmountByUserProjection> getOrderAmountsByUser(OrderStates orderState);

}
