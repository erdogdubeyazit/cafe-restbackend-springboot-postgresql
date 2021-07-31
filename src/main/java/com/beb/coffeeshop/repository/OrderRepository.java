package com.beb.coffeeshop.repository;

import java.util.Optional;

import com.beb.coffeeshop.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Search order by number
     * 
     * @param orderNumber generated number of the order
     * @return Order. Consider result can be null.
     */
    Optional<Order> findByOrderNumber(String orderNumber);

}
