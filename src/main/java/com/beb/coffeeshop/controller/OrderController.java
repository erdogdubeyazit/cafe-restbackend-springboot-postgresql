package com.beb.coffeeshop.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.beb.coffeeshop.exception.OrderAccessDeniedException;
import com.beb.coffeeshop.exception.OrderNumberNotFoundException;
import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Order;
import com.beb.coffeeshop.presentation.payload.order.AddBeverageToOrderPayload;
import com.beb.coffeeshop.presentation.payload.order.AddToppingToBeverage;
import com.beb.coffeeshop.presentation.result.OrderResult;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;
import com.beb.coffeeshop.security.ApiUserDetails;
import com.beb.coffeeshop.security.CurrentUser;
import com.beb.coffeeshop.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles order operations
 * 
 * @author Beyazit
 * @category Controller
 */
@RestController
@RequestMapping("/api/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * Creattes order
     * 
     * @param currentUser authenticated user
     * @return HttpStatus code
     */
    @PostMapping(path = "orders")
    @RolesAllowed({ "ROLE_USER" })
    public ResponseEntity<ApiResult> create(@CurrentUser ApiUserDetails currentUser) {
        try {
            Order order = orderService.create(currentUser.getUserId());
            return OrderResult.build(order);
        } catch (ServiceException e) {
            logger.error(String.format("Order create operation failed. [User info: %s ]", currentUser.getUsername()),
                    e);
            return Result.failure("Order can not be created");
        }
    }

    /**
     * Retrieves order info
     * 
     * @param orderNumber order to retrieve
     * @param currentUser authenticated user
     * @return OrderResult
     */
    @GetMapping(path = "orders/{orderNumber}")
    public ResponseEntity<ApiResult> getOrder(@PathVariable(name = "orderNumber", required = true) String orderNumber,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            Order order = orderService.findByOrderNumber(orderNumber, currentUser.getUserId());
            return OrderResult.build(order);
        } catch (OrderAccessDeniedException e) {
            logger.error(String.format("Access denied for Order with number:%s", orderNumber), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResult
                    .error(String.format("Access denied for Order with number:%s", orderNumber), "Access denied"));
        } catch (OrderNumberNotFoundException e) {
            logger.error(String.format("Order number not found {Details:%s} [User info: %s ]", orderNumber,
                    currentUser.getUsername()), e);
            return Result.failure("Order number not found");
        } catch (ServiceException e) {
            logger.error(String.format("`OrderController.addBeverageToOrder()` failed. {Details:%s} [User info: %s ]",
                    orderNumber, currentUser.getUsername()), e);
            return Result.failure("Order data can not be fetched.");
        }
    }

    /**
     * Adds beverage to order
     * 
     * @param orderNumber order to add beverage
     * @param payload     AddBeverageToOrderPayload
     * @param currentUser authenticated user
     * @return OrderResult
     */
    @PostMapping(path = "orders/{orderNumber}/beverages")
    @RolesAllowed({ "ROLE_USER" })
    public ResponseEntity<ApiResult> addBeverageToOrder(
            @PathVariable(name = "orderNumber", required = true) String orderNumber,
            @RequestBody @Valid AddBeverageToOrderPayload payload, @CurrentUser ApiUserDetails currentUser) {

        try {
            Order order = orderService.addBeverageToOrder(orderNumber, payload.getBeverageName(), payload.getCount(),
                    currentUser.getUserId());
            return OrderResult.build(order);
        } catch (Exception e) {
            logger.error(String.format("`OrderController.addBeverageToOrder()` failed. {Details:%s} [User info: %s ]",
                    String.format("orderNumber:%s, payload:%s", orderNumber, payload), currentUser.getUsername()), e);
            return Result.failure("Beverage can not be added to the order");
        }
    }

    /**
     * Removes beverage and its toppings from the order
     * 
     * @param orderNumber order to operate on
     * @param orderItemId order item to remove
     * @param currentUser authenticated user
     * @return OrderResult
     */
    @DeleteMapping(path = "orders/{orderNumber}/beverages/{orderItemId}")
    @RolesAllowed({ "ROLE_USER" })
    public ResponseEntity<ApiResult> removeBeverageFromOrder(
            @PathVariable(name = "orderNumber", required = true) String orderNumber,
            @PathVariable(name = "orderItemId", required = true) Long orderItemId,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            Order order = orderService.removeBeverageFromOrder(orderNumber, orderItemId, currentUser.getUserId());
            return OrderResult.build(order);
        } catch (Exception e) {
            logger.error(
                    String.format("`OrderController.removeBeverageFromOrder()` failed. {Details:%s} [User info: %s ]",
                            String.format("orderNumber:%s, orderItemId:%d", orderNumber, orderItemId),
                            currentUser.getUsername()),
                    e);
            return Result.failure("Beverage can not be removed from the order");
        }
    }

    /**
     * Adds topping to beverage in order
     * 
     * @param orderNumber order to operate on
     * @param orderItemId order item to add topping to its beverge
     * @param payload     AddToppingToBeverage
     * @param currentUser authenticated user
     * @return OrderResult
     */
    @PostMapping(path = "orders/{orderNumber}/beverages/{orderItemId}/toppings")
    @RolesAllowed({ "ROLE_USER" })
    public ResponseEntity<ApiResult> addToppingToBeverage(
            @PathVariable(name = "orderNumber", required = true) String orderNumber,
            @PathVariable(name = "orderItemId", required = true) Long orderItemId,
            @RequestBody @Valid AddToppingToBeverage payload, @CurrentUser ApiUserDetails currentUser) {

        try {
            Order order = orderService.addToppingToBeverage(orderNumber, orderItemId, payload.getToppingName(),
                    currentUser.getUserId());
            return OrderResult.build(order);
        } catch (Exception e) {
            logger.error(String.format("`OrderController.addToppingToBeverage()` failed. {Details:%s} [User info: %s ]",
                    String.format("orderNumber:%s, orderItemId:%d, payload:%s", orderNumber, orderItemId, payload),
                    currentUser.getUsername()), e);
            return Result.failure("Topping can not be added to the beverage");
        }

    }

    /**
     * REmoves topping from order item
     * 
     * @param orderNumber order to operate on
     * @param orderItemId order item to remove topping from
     * @param toppingName topping name to remove
     * @param currentUser authenticated user
     * @return OrderResult
     */
    @DeleteMapping(path = "orders/{orderNumber}/beverages/{orderItemId}/toppings/{toppingName}")
    @RolesAllowed({ "ROLE_USER" })
    public ResponseEntity<ApiResult> removeToppingFromBeverage(
            @PathVariable(name = "orderNumber", required = true) String orderNumber,
            @PathVariable(name = "orderItemId", required = true) Long orderItemId,
            @PathVariable(name = "toppingName", required = true) String toppingName,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            Order order = orderService.removeToppingFromBeverage(orderNumber, orderItemId, toppingName,
                    currentUser.getUserId());
            return OrderResult.build(order);
        } catch (Exception e) {
            logger.error(
                    String.format("`OrderController.removeToppingFromBeverage()` failed. {Details:%s} [User info: %s ]",
                            String.format("orderNumber:%s, orderItemId:%d, toppingName:%s", orderNumber, orderItemId,
                                    toppingName),
                            currentUser.getUsername()),
                    e);
            return Result.failure("Topping can not be removed from the beverage");
        }
    }

    /**
     * Completes order. Discount calculation is done at complete event.
     * 
     * @param orderNumber order to complete
     * @param currentUser authenticated user
     * @return OrderResult
     */
    @PatchMapping(path = "orders/{orderNumber}/complete")
    @RolesAllowed({ "ROLE_USER" })
    public ResponseEntity<ApiResult> completeOrder(
            @PathVariable(name = "orderNumber", required = true) String orderNumber,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            Order order = orderService.completeOrder(orderNumber, currentUser.getUserId());
            return OrderResult.build(order);
        } catch (Exception e) {
            logger.error(String.format("`OrderController.completeOrder()` failed. {Details:%s} [User info: %s ]",
                    String.format("orderNumber:%s", orderNumber), currentUser.getUsername()), e);
            return Result.failure("Order can not be completed");
        }
    }

    /**
     * Cancels order. Once an order is cancelled there is no action to do on it
     * 
     * @param orderNumber order to cancal
     * @param currentUser authenticated user
     * @return OrderResult
     */
    @PatchMapping(path = "orders/{orderNumber}/cancel")
    @RolesAllowed({ "ROLE_USER" })
    public ResponseEntity<ApiResult> cancelOrder(
            @PathVariable(name = "orderNumber", required = true) String orderNumber,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            Order order = orderService.cancelOrder(orderNumber, currentUser.getUserId());
            return OrderResult.build(order);
        } catch (Exception e) {
            logger.error(String.format("`OrderController.cancelOrder()` failed. {Details:%s} [User info: %s ]",
                    String.format("orderNumber:%s", orderNumber), currentUser.getUsername()), e);
            return Result.failure("Order can not be cancelled");
        }
    }

}
