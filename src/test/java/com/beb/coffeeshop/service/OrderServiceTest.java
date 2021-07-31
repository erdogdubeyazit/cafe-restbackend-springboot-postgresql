package com.beb.coffeeshop.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.beb.coffeeshop.exception.OrderAccessDeniedException;
import com.beb.coffeeshop.exception.OrderNumberNotFoundException;
import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Order;
import com.beb.coffeeshop.model.OrderStates;
import com.beb.coffeeshop.model.User;
import com.beb.coffeeshop.repository.BeverageRepository;
import com.beb.coffeeshop.repository.OrderItemRepository;
import com.beb.coffeeshop.repository.OrderRepository;
import com.beb.coffeeshop.repository.ToppingRepository;
import com.beb.coffeeshop.repository.UserRepository;
import com.beb.coffeeshop.service.impl.OrderServiceImpl;
import com.beb.coffeeshop.util.discount.PromotionStrategyFactory;
import com.beb.coffeeshop.util.order.OrderNumberGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderNumberGenerator orderNumberGeneratorMock;
    @Mock
    private OrderRepository orderRepositoryMock;
    @Mock
    private BeverageRepository beverageRepositoryMock;
    @Mock
    private ToppingRepository toppingRepositoryMock;
    @Mock
    private OrderItemRepository orderItemRepositoryMock;
    @Mock
    private PromotionStrategyFactory promotionStrategyFactoryMock;
    @Mock
    private UserRepository userRepositoryMock;

    @Before
    public void setUp() {
        orderService = new OrderServiceImpl(orderNumberGeneratorMock, orderRepositoryMock, beverageRepositoryMock,
                toppingRepositoryMock, orderItemRepositoryMock, promotionStrategyFactoryMock, userRepositoryMock);
    }

    @Test
    public void test_create() {
        assertThrows(Exception.class, () -> orderService.create(null));
        when(orderNumberGeneratorMock.generate()).thenReturn("TEST");
        when(orderItemRepositoryMock.save(any())).thenReturn(Optional.of(new Order()));
        assertThrows(Exception.class, () -> orderService.create(-1L));

    }

    @Test
    public void addBeverageToOrder_with_missing_parameters_should_fail() {
        assertThrows(Exception.class, () -> orderService.addBeverageToOrder(null, "beverageName", 1, -1L));
        assertThrows(Exception.class, () -> orderService.addBeverageToOrder("null", null, 1, -1L));
        assertThrows(Exception.class, () -> orderService.addBeverageToOrder("null", "null", null, -1L));
        assertThrows(Exception.class, () -> orderService.addBeverageToOrder("null", "null", 1, null));
    }

    @Test(expected = ServiceException.class)
    public void addBeverageToOrder_with_zero_or_negative_count_should_fail()
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        try {
            orderService.addBeverageToOrder("orderNumber", "beverageName", 0, -1L);
        } catch (ServiceException e) {
            assertEquals("Invalid beverage count requested", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ServiceException.class)
    public void addBeverageToOrder_with_unreal_beveragename_should_fail()
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {

        try {
            Order order = new Order();
            order.setOrderNumber("TEST");
            User user = new User();
            user.setId(-1L);
            order.setUser(user);
            when(orderRepositoryMock.findByOrderNumber(anyString())).thenReturn(Optional.of(order));
            when(beverageRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
            orderService.addBeverageToOrder("TEST", "beverageName", 2, -1L);
        } catch (ServiceException e) {
            assertEquals("Beverage not found by name:beverageName", e.getMessage());
            throw e;
        }
    }

    @Test
    public void removeBeverageFromOrder_with_missing_parameters_should_fail() {
        assertThrows(Exception.class, () -> orderService.removeBeverageFromOrder(null, -1L, -1L));
        assertThrows(Exception.class, () -> orderService.removeBeverageFromOrder("null", null, -1L));
        assertThrows(Exception.class, () -> orderService.removeBeverageFromOrder("null", -1L, null));
    }

    @Test
    public void addToppingToBeverage_with_missing_parameters_shoulf_fail() {
        assertThrows(Exception.class, () -> orderService.addToppingToBeverage(null, -1L, "toppingName", -1L));
        assertThrows(Exception.class, () -> orderService.addToppingToBeverage("null", null, "toppingName", -1L));
        assertThrows(Exception.class, () -> orderService.addToppingToBeverage("null", -1L, null, -1L));
        assertThrows(Exception.class, () -> orderService.addToppingToBeverage("null", -1L, "toppingName", null));
    }

    @Test(expected = ServiceException.class)
    public void addToppingToBeverage_with_unreal_orderitemnumber_should_fail()
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        when(orderItemRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        orderService.addToppingToBeverage("orderNumber", -1L, "toppingName", -1L);

    }

    @Test
    public void removeToppingFromBeverage_with_missing_parameters_should_fail() {
        assertThrows(Exception.class, () -> orderService.removeBeverageFromOrder(null, -1L, -1L));
        assertThrows(Exception.class, () -> orderService.removeBeverageFromOrder("null", null, -1L));
        assertThrows(Exception.class, () -> orderService.removeBeverageFromOrder("null", -1L, null));
    }

    @Test(expected = ServiceException.class)
    public void completeOrder_with_cancelled_order_should_fail()
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        Order order = new Order();
        order.setOrderState(OrderStates.CANCELLED);
        User user = new User();
        user.setId(-1L);
        order.setUser(user);
        order.setOrderNumber("TEST");
        when(orderRepositoryMock.findByOrderNumber(anyString())).thenReturn(Optional.of(order));
        orderService.completeOrder("TEST", -1L);
    }

    @Test(expected = ServiceException.class)
    public void cancelOrder_with_cancelled_order_should_fail()
            throws OrderAccessDeniedException, OrderNumberNotFoundException, ServiceException {
        Order order = new Order();
        order.setOrderState(OrderStates.CANCELLED);
        User user = new User();
        user.setId(-1L);
        order.setUser(user);
        order.setOrderNumber("TEST");
        when(orderRepositoryMock.findByOrderNumber(anyString())).thenReturn(Optional.of(order));
        orderService.completeOrder("TEST", -1L);
    }

    @Test(expected = OrderNumberNotFoundException.class)
    public void test_findByOrderNumber()
            throws OrderAccessDeniedException, ServiceException, OrderNumberNotFoundException {
        assertThrows(Exception.class, () -> orderService.findByOrderNumber("orderNumber", null));
        assertThrows(Exception.class, () -> orderService.findByOrderNumber(null, -1L));

        try {
            when(orderRepositoryMock.findByOrderNumber(anyString())).thenReturn(Optional.empty());
            orderService.findByOrderNumber("orderNumber", -1L);
        } catch (OrderNumberNotFoundException e) {
            e.printStackTrace();
            assertEquals("Order number not found. Order number: orderNumber", e.getMessage());
            throw e;
        }
    }

}
