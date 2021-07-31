package com.beb.coffeeshop.service;

import com.beb.coffeeshop.exception.OrderAccessDeniedException;
import com.beb.coffeeshop.exception.OrderNumberNotFoundException;
import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Order;

/**
 * Service for operations on Order
 * 
 * @author Beyazit
 * @category Service
 * @implNote All operations retun Order because of a Software Requirement.
 * @see "Backend API Backend Technical Aseesment Document"
 */
public interface OrderService {

        /**
         * Creates order
         * 
         * @param userId
         * @return Order
         * @throws ServiceException
         */
        Order create(Long userId) throws ServiceException;

        /**
         * Searches orders by order number.
         * 
         * @implSpec checks access of the user
         * @param orderNumber number of the order
         * @param userId      id of the user
         * @return Order
         * @throws OrderAccessDeniedException
         * @throws OrderNumberNotFoundException
         * @throws ServiceException
         */
        Order findByOrderNumber(String orderNumber, Long userId)
                        throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException;

        /**
         * Adds beverage to order
         * 
         * @implSpec checks access of the user
         * @param orderNumber  number of the order
         * @param beverageName name of the beverage
         * @param count        requested number of beverage insertions
         * @param userId       id of the user
         * @return Order
         * @throws OrderAccessDeniedException
         * @throws OrderNumberNotFoundException
         * @throws ServiceException
         */
        Order addBeverageToOrder(String orderNumber, String beverageName, Integer count, Long userId)
                        throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException;

        /**
         * Removes beverage and toppings added with
         * 
         * @implSpec checks access of the user
         * @param orderNumber number of the order
         * @param orderItemId id of the order item
         * @param userId      id of the user
         * @return Order
         * @throws OrderAccessDeniedException
         * @throws ServiceException
         * @throws OrderNumberNotFoundException
         */
        Order removeBeverageFromOrder(String orderNumber, Long orderItemId, Long userId)
                        throws OrderAccessDeniedException, ServiceException, OrderNumberNotFoundException;

        /**
         * Adds topping to order item
         * 
         * @param orderNumber number of the order
         * @param orderItemId id of the order item
         * @param toppingName name of the topping to add
         * @param userId      id of the user
         * @return Order
         * @throws OrderAccessDeniedException
         * @throws ServiceException
         * @throws OrderNumberNotFoundException
         */
        Order addToppingToBeverage(String orderNumber, Long orderItemId, String toppingName, Long userId)
                        throws OrderAccessDeniedException, ServiceException, OrderNumberNotFoundException;

        /**
         * Removes topping from order item
         * 
         * @param orderNumber number of the order
         * @param orderItemId id of the order item
         * @param toppingName name of the topping to add
         * @param userId      id of the user
         * @return Order
         * @throws OrderAccessDeniedException
         * @throws ServiceException
         * @throws OrderNumberNotFoundException
         */
        Order removeToppingFromBeverage(String orderNumber, Long orderItemId, String toppingName, Long userId)
                        throws OrderAccessDeniedException, ServiceException, OrderNumberNotFoundException;;

        /**
         * Completes order
         * 
         * @implSpec Discount calculations are done at this phase
         * @implSpec checks access of the user
         * @param orderNumber number of the order
         * @param userId      id of the user
         * @return Order
         * @throws OrderAccessDeniedException
         * @throws OrderNumberNotFoundException
         * @throws ServiceException
         * @throws PromotionStrageyException
         */
        Order completeOrder(String orderNumber, Long userId) throws OrderAccessDeniedException,
                        OrderNumberNotFoundException, ServiceException;

        /**
         * Cancels order
         * 
         * @implSpec Once an order cancelled, no operation will be allowed on it
         * @implSpec checks access of the user
         * @param orderNumber number of the order
         * @param userId      id of the user
         * @return Order
         * @throws OrderAccessDeniedException
         * @throws OrderNumberNotFoundException
         * @throws ServiceException
         */
        Order cancelOrder(String orderNumber, Long userId)
                        throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException;
}
