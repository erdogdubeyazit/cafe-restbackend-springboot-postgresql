package com.beb.coffeeshop.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.model.Order;
import com.beb.coffeeshop.model.OrderItem;
import com.beb.coffeeshop.model.Topping;
import com.beb.coffeeshop.util.discount.PercentagePromotionStrategy;
import com.beb.coffeeshop.util.discount.PromotionStrategy;
import com.beb.coffeeshop.util.discount.PromotionStrategyFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class PromotionTest {

    private PromotionStrategyFactory promotionStrategyFactory;

    @Before
    public void setUp() {
        promotionStrategyFactory = new PromotionStrategyFactory();
    }

    @Test
    public void total_cart_more_than_exact_amount_should_return_rate_promotion() {
        Beverage beverageMock = Mockito.mock(Beverage.class);

        when(beverageMock.getPrice()).thenReturn(BigDecimal.valueOf(100));

        OrderItem orderItem = Mockito.mock(OrderItem.class);
        when(orderItem.getBeverage()).thenReturn(beverageMock);
        when((orderItem.getToppings())).thenReturn(new HashSet<>());

        Order order = new Order();
        order.getItems().add(orderItem);
        PromotionStrategy promotion = promotionStrategyFactory.getPromotion(order);

        BigDecimal expected = ((PercentagePromotionStrategy) (promotion)).getDiscountRate()
                .multiply(BigDecimal.valueOf(100));

        assertEquals(expected.compareTo(promotion.getDiscountAmount().setScale(2, RoundingMode.DOWN)), 0);

    }

    @Test
    public void buy_3_count_of_product_should_return_cheapest_free_promotion() {
        Beverage beverage = new Beverage();
        beverage.setName("TEST Beverage");

        beverage.setPrice(BigDecimal.valueOf(0.1));

        Topping topping1 = new Topping();
        topping1.setPrice(BigDecimal.valueOf(0.01));
        topping1.setName("TEST1");

        Topping topping2 = new Topping();
        topping2.setPrice(BigDecimal.valueOf(0.02));
        topping2.setName("TEST2");

        Set<Topping> toppings = new HashSet<>();
        toppings.add(topping1);
        toppings.add(topping2);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1l);
        orderItem1.setBeverage(beverage);
        orderItem1.setToppings(toppings);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2l);
        orderItem2.setBeverage(beverage);
        orderItem2.setToppings(toppings);

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setId(3l);
        orderItem3.setBeverage(beverage);
        orderItem3.setToppings(toppings);

        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem1);
        items.add(orderItem2);
        items.add(orderItem3);

        Order order = new Order();
        order.setItems(items);

        PromotionStrategy promotion = promotionStrategyFactory.getPromotion(order);

        assertEquals(promotion.getDiscountAmount().compareTo(topping1.getPrice().setScale(2, RoundingMode.DOWN)), 0);
    }

    @Test
    public void when_promotions_intersect_biggest_should_work() {

        Beverage beverage = new Beverage();
        beverage.setName("TEST Beverage");

        beverage.setPrice(BigDecimal.valueOf(10));

        Topping topping1 = new Topping();
        topping1.setPrice(BigDecimal.valueOf(10));
        topping1.setName("TEST1");

        Topping topping2 = new Topping();
        topping2.setPrice(BigDecimal.valueOf(0.02));
        topping2.setName("TEST2");

        Set<Topping> toppings = new HashSet<>();
        toppings.add(topping1);
        toppings.add(topping2);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setId(1l);
        orderItem1.setBeverage(beverage);
        orderItem1.setToppings(toppings);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2l);
        orderItem2.setBeverage(beverage);
        orderItem2.setToppings(toppings);

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setId(3l);
        orderItem3.setBeverage(beverage);
        orderItem3.setToppings(toppings);

        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem1);
        items.add(orderItem2);
        items.add(orderItem3);

        Order order = new Order();
        order.setItems(items);

        PromotionStrategy promotion = promotionStrategyFactory.getPromotion(order);

        try {
            PromotionStrategy p = ((PercentagePromotionStrategy) promotion);
            assertNotNull(p);
        } catch (Exception e) {
            fail("No exception expected");
        }

    }

}
