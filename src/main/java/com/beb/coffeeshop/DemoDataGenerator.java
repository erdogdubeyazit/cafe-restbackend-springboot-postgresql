package com.beb.coffeeshop;

import java.math.BigDecimal;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Currency;
import com.beb.coffeeshop.service.BeverageService;
import com.beb.coffeeshop.service.ToppingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Generates data provided in Software Requirements documnet
 * 
 * @see "Backend API Backend Technical Aseesment Document"
 */
@Component
public class DemoDataGenerator implements ApplicationRunner {

    @Autowired
    private BeverageService beverageService;
    @Autowired
    private ToppingService toppingService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        generateDrinks();
        generateToppings();

    }

    private void generateDrinks() throws ServiceException {
        beverageService.save("Black Coffee", BigDecimal.valueOf(4.00), Currency.EUR.name());
        beverageService.save("Latte", BigDecimal.valueOf(5.00), Currency.EUR.name());
        beverageService.save("Mocha", BigDecimal.valueOf(6.00), Currency.EUR.name());
        beverageService.save("Tea", BigDecimal.valueOf(3.00), Currency.EUR.name());
    }

    private void generateToppings() throws ServiceException {
        toppingService.save("Milk", BigDecimal.valueOf(2.00), Currency.EUR.name());
        toppingService.save("Hazelnut syrup", BigDecimal.valueOf(3.00), Currency.EUR.name());
        toppingService.save("Chocolate sauce", BigDecimal.valueOf(5.00), Currency.EUR.name());
        toppingService.save("Lemon", BigDecimal.valueOf(2.00), Currency.EUR.name());
    }

}
