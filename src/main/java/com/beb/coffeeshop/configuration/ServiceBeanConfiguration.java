package com.beb.coffeeshop.configuration;

import com.beb.coffeeshop.repository.BeverageRepository;
import com.beb.coffeeshop.repository.OrderItemRepository;
import com.beb.coffeeshop.repository.OrderRepository;
import com.beb.coffeeshop.repository.ToppingRepository;
import com.beb.coffeeshop.repository.UserRepository;
import com.beb.coffeeshop.repository.statistic.OrderItemStatisticsRepository;
import com.beb.coffeeshop.service.BeverageService;
import com.beb.coffeeshop.service.OrderService;
import com.beb.coffeeshop.service.StatisticsService;
import com.beb.coffeeshop.service.ToppingService;
import com.beb.coffeeshop.service.UserService;
import com.beb.coffeeshop.service.impl.BeverageServiceImpl;
import com.beb.coffeeshop.service.impl.OrderServiceImpl;
import com.beb.coffeeshop.service.impl.StatisticsServiceImpl;
import com.beb.coffeeshop.service.impl.ToppingServiceImpl;
import com.beb.coffeeshop.service.impl.UserServiceImpl;
import com.beb.coffeeshop.util.discount.PromotionStrategyFactory;
import com.beb.coffeeshop.util.order.OrderNumberGenerator;
import com.beb.coffeeshop.util.order.impl.OrderNumberGeneratorImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Service bean configuration
 * 
 * @author Beyazit
 * @category Configuration
 * @implNote This is for instantiating service beans with their dependencies.
 *           This is used insead of autowiring to leverage testability and
 *           transparancy of dependencies
 */
@Configuration
public class ServiceBeanConfiguration {

    @Autowired
    private BeverageRepository beverageRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemStatisticsRepository orderItemStatisticsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private OrderNumberGenerator orderNumberGenerator;
    private PromotionStrategyFactory promotionStrategyFactory;

    public ServiceBeanConfiguration() {
        orderNumberGenerator = new OrderNumberGeneratorImpl();
        promotionStrategyFactory = new PromotionStrategyFactory();
    }

    /**
     * @return BeverageService singleton instance
     */
    @Bean
    public BeverageService beverageService() {
        return new BeverageServiceImpl(beverageRepository);
    }

    /**
     * @return ToppingService singleton instance
     */
    @Bean
    public ToppingService toppingService() {
        return new ToppingServiceImpl(toppingRepository);
    }

    /**
     * @return OrderService singleton instance
     */
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(orderNumberGenerator, orderRepository, beverageRepository, toppingRepository,
                orderItemRepository, promotionStrategyFactory, userRepository);
    }

    /**
     * @return UserService singleton instance
     */
    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }

    /**
     * @return StatisticsService singleton instance
     */
    @Bean
    public StatisticsService statisticsService() {
        return new StatisticsServiceImpl(orderItemStatisticsRepository);
    }
}
