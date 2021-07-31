package com.beb.coffeeshop.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import com.beb.coffeeshop.exception.OrderAccessDeniedException;
import com.beb.coffeeshop.exception.OrderNumberNotFoundException;
import com.beb.coffeeshop.exception.OrderStateException;
import com.beb.coffeeshop.exception.PromotionException;
import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.model.Discount;
import com.beb.coffeeshop.model.Order;
import com.beb.coffeeshop.model.OrderItem;
import com.beb.coffeeshop.model.OrderStates;
import com.beb.coffeeshop.model.Topping;
import com.beb.coffeeshop.model.User;
import com.beb.coffeeshop.repository.BeverageRepository;
import com.beb.coffeeshop.repository.OrderItemRepository;
import com.beb.coffeeshop.repository.OrderRepository;
import com.beb.coffeeshop.repository.ToppingRepository;
import com.beb.coffeeshop.repository.UserRepository;
import com.beb.coffeeshop.service.OrderService;
import com.beb.coffeeshop.util.discount.PromotionStrategy;
import com.beb.coffeeshop.util.discount.PromotionStrategyExecutor;
import com.beb.coffeeshop.util.discount.PromotionStrategyFactory;
import com.beb.coffeeshop.util.order.OrderNumberGenerator;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class OrderServiceImpl implements OrderService {

    private OrderNumberGenerator orderNumberGenerator;

    private OrderRepository orderRepository;

    private BeverageRepository beverageRepository;
    private ToppingRepository toppingRepository;

    private OrderItemRepository orderItemRepository;

    private PromotionStrategyFactory promotionStrategyFactory;

    private UserRepository userRepository;

    public OrderServiceImpl(OrderNumberGenerator orderNumberGenerator, OrderRepository orderRepository,
            BeverageRepository beverageRepository, ToppingRepository toppingRepository,
            OrderItemRepository orderItemRepository, PromotionStrategyFactory promotionStrategyFactory,
            UserRepository userRepository) {
        this.orderNumberGenerator = orderNumberGenerator;
        this.orderRepository = orderRepository;
        this.beverageRepository = beverageRepository;
        this.toppingRepository = toppingRepository;
        this.orderItemRepository = orderItemRepository;
        this.promotionStrategyFactory = promotionStrategyFactory;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Order create(Long userId) throws ServiceException {
        Assert.notNull(userId, "userId can not be null");
        try {
            Order order = new Order();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception(String.format("User not found by id:%d", userId)));
            order.setUser(user);
            order.setOrderNumber(orderNumberGenerator.generate());

            changeState(order, OrderStates.OPEN);

            return orderRepository.save(order);
        } catch (Exception e) {
            throw new ServiceException("`OrderServiceImpl.create()` failed", e);
        }
    }

    @Override
    public Order addBeverageToOrder(String orderNumber, String beverageName, Integer count, Long userId)
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.hasText(beverageName, "beverageName can not be empty");
        Assert.notNull(beverageName, "count can not be null");
        Assert.notNull(userId, "userId can not be null");

        if (count <= 0)
            throw new ServiceException("Invalid beverage count requested");

        Order order = findByOrderNumber(orderNumber, userId);

        Beverage beverage = beverageRepository.findByName(beverageName)
                .orElseThrow(() -> new ServiceException(String.format("Beverage not found by name:%s", beverageName)));

        for (int i = 0; i < count; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBeverage(beverage);
            order.getItems().add(orderItem);
        }

        try {
            // Set order state to open
            changeState(order, OrderStates.OPEN);
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new ServiceException("`OrderServiceImpl.addBeverageToOrder()` failed.", e);
        }
    }

    @Override
    public Order removeBeverageFromOrder(String orderNumber, Long orderItemId, Long userId)
            throws OrderAccessDeniedException, ServiceException, OrderNumberNotFoundException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.notNull(orderItemId, "orderItemId can not be null");
        Assert.notNull(userId, "userId can not be null");

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new ServiceException(
                String.format("Order item not found. {orderItemId:%d, userId: %d}", orderItemId, userId)));

        // Check whether the item id belongs to correct order
        checkOrderItemBelongsToOrder(orderItem, orderNumber);

        Order order = orderItem.getOrder();

        checkAccess(order, userId);

        boolean removed = order.getItems().remove(orderItem);

        if (removed == false)
            throw new ServiceException("OrderItem can not be deleted");

        try {
            // Set order state to open
            changeState(order, OrderStates.OPEN);
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new ServiceException("`OrderServiceImpl.removeBeverageFromOrder()` failed.", e);
        }
    }

    @Override
    public Order addToppingToBeverage(String orderNumber, Long orderItemId, String toppingName, Long userId)
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.notNull(orderItemId, "orderItemId can not be null");
        Assert.hasText(toppingName, "toppingName can not be empty");
        Assert.notNull(userId, "userId can not be null");

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ServiceException(String.format("Order item not found by id: %d", orderItemId)));

        // Check whether the item id belongs to correct order
        checkOrderItemBelongsToOrder(orderItem, orderNumber);

        checkAccess(orderItem.getOrder(), userId);

        Topping topping = toppingRepository.findByName(toppingName)
                .orElseThrow(() -> new ServiceException(String.format("Topping not found by name: %s", toppingName)));

        // Only one same topping is accepted
        boolean toppingAdded = orderItem.getToppings().add(topping);
        if (!toppingAdded)
            throw new ServiceException("Add topping to beverage failed");

        try {
            // Set order state to open
            changeState(orderItem.getOrder(), OrderStates.OPEN);
            orderItem = orderItemRepository.save(orderItem);
            return orderItem.getOrder();
        } catch (Exception e) {
            throw new ServiceException("`OrderServiceImpl.addToppingToBeverage()` failed", e);
        }

    }

    @Override
    public Order removeToppingFromBeverage(String orderNumber, Long orderItemId, String toppingName, Long userId)
            throws OrderAccessDeniedException, ServiceException, OrderNumberNotFoundException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.notNull(orderItemId, "orderItemId can not be null");
        Assert.hasText(toppingName, "toppingName can not be empty");
        Assert.notNull(userId, "userId can not be null");

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ServiceException(String.format("Order item not found by id: %d", orderItemId)));

        // Check whether the item id belongs to correct order
        checkOrderItemBelongsToOrder(orderItem, orderNumber);

        checkAccess(orderItem.getOrder(), userId);

        Topping topping = toppingRepository.findByName(toppingName)
                .orElseThrow(() -> new ServiceException(String.format("Topping not found by name: %s", toppingName)));
        boolean removed = orderItem.getToppings().remove(topping);
        if (!removed)
            throw new ServiceException("Remove topping from beverage failed");

        try {
            // Set order state to open
            changeState(orderItem.getOrder(), OrderStates.OPEN);
            orderItem = orderItemRepository.save(orderItem);
            return orderItem.getOrder();
        } catch (Exception e) {
            throw new ServiceException("`OrderServiceImpl.addToppingToBeverage()` failed", e);
        }
    }

    @Override
    public Order completeOrder(String orderNumber, Long userId)
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.notNull(userId, "userId can not be null");

        Order order = findByOrderNumber(orderNumber, userId);

        // Discount logic
        PromotionStrategy promotion = promotionStrategyFactory.getPromotion(order);
        if (promotion != null) {
            Discount discount;
            if (order.getDiscount() != null)
                discount = order.getDiscount();
            else {
                discount = new Discount();
                discount.setOrder(order);
            }
            PromotionStrategyExecutor promotionStrategyExecutor = new PromotionStrategyExecutor(discount);
            try {
                Discount executedStrategy = promotionStrategyExecutor.executeStrategy(promotion);
                order.setDiscount(executedStrategy);
            } catch (PromotionException e) {
                order.setDiscount(null);
            }
        } else {
            order.setDiscount(null);
        }

        try {
            // Set order state to completed
            changeState(order, OrderStates.COMPLETED);
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new ServiceException("`OrderServiceImpl.completeOrder()` failed", e);
        }
    }

    @Override
    public Order cancelOrder(String orderNumber, Long userId)
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.notNull(userId, "userId can not be null");

        Order order = findByOrderNumber(orderNumber, userId);
        try {
            // Set order state to cancelled
            changeState(order, OrderStates.CANCELLED);
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new ServiceException("`OrderServiceImpl.cancelOrder()` failed", e);
        }

    }

    @Override
    public Order findByOrderNumber(String orderNumber, Long userId)
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.notNull(userId, "userId can not be null");

        Optional<Order> order;
        try {
            order = orderRepository.findByOrderNumber(orderNumber);
        } catch (Exception e) {
            throw new ServiceException("Error while fetching order info.", e);
        }

        if (!order.isPresent())
            throw new OrderNumberNotFoundException(
                    String.format("Order number not found. Order number: %s", orderNumber));

        checkAccess(order.get(), userId);

        return order.get();
    }

    private void checkAccess(Order order, Long userId) throws OrderAccessDeniedException {
        Assert.notNull(order, "orderIteordermId can not be null");
        Assert.notNull(userId, "userId can not be null");

        if (order.getUser().getId() != userId)
            throw new OrderAccessDeniedException(
                    String.format("Order(id:%d) is not accessible for this user(id:%d)", order.getId(), userId));
    }

    /**
     * Sice paramters come from api. They must be double checked
     * 
     * @param orderItem
     * @param orderNumber
     * @throws OrderNumberNotFoundException
     */
    private void checkOrderItemBelongsToOrder(OrderItem orderItem, String orderNumber)
            throws OrderNumberNotFoundException {
        Assert.hasText(orderNumber, "orderNumber can not be empty");
        Assert.notNull(orderItem, "orderItem can not be null");

        if (!orderItem.getOrder().getOrderNumber().equals(orderNumber))
            throw new OrderNumberNotFoundException(String
                    .format("Order number:%s and orderItemId:%d does not correspond", orderNumber, orderItem.getId()));
    }

    /**
     * Order tansition logic
     * 
     * @param order
     * @param targetState
     * @return Order
     * @throws OrderStateException
     */
    private Order changeState(Order order, OrderStates targetState) throws OrderStateException {
        Assert.notNull(order, "order can not be null");
        Assert.notNull(targetState, "targetState can not be null");

        if (order.getOrderState() == OrderStates.CANCELLED)
            throw new OrderStateException("Order is cancelled");
        else if (order.getOrderState() == OrderStates.COMPLETED && targetState == OrderStates.COMPLETED)
            throw new OrderStateException("Order is already completed");
        else if (order.getOrderState() == OrderStates.OPEN && targetState == OrderStates.OPEN)
            return order;
        else {
            order.setOrderState(targetState);
            if (targetState == OrderStates.OPEN)
                order.setOrderTotal(null);
            if (targetState == OrderStates.COMPLETED)
                order.setOrderTotal(calculateOrderTotal(order));
            return order;
        }
    }

    /**
     * Calculations are done outside of the anemic classes
     * 
     * @param order
     * @return
     */
    private BigDecimal calculateOrderTotal(Order order) {
        Assert.notNull(order, "order can not be null");

        BigDecimal beverageSum = order.getItems().stream().map(i -> i.getBeverage().getPrice()).reduce(BigDecimal.ZERO,
                (a, b) -> a.add(b));
        BigDecimal toppingSum = order.getItems().stream().flatMap(i -> i.getToppings().stream()).map(t -> t.getPrice())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal total = beverageSum.add(toppingSum);
        if (order.getDiscount() != null)
            total = total.subtract(order.getDiscount().getDiscountAmount());

        return total;
    }

}
