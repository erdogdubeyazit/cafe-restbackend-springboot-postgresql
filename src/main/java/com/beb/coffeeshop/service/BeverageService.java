package com.beb.coffeeshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Beverage;

import org.springframework.data.domain.Page;

/**
 * Service for beverage operations.
 * 
 * @author Beyazit
 * @category Service
 */
public interface BeverageService {

    /**
     * 
     * @return list of beverages
     */
    List<Beverage> getAll();

    /**
     * @param pageNo     page that is requested to work on
     * @param pageSize   item count per page
     * @param sortColumn natural ordering by specific column
     * @return Pageable list of beverages
     */
    Page<Beverage> getAll(Integer pageNo, Integer pageSize, String sortColumn);

    /**
     * 
     * @param id beverage id
     * @return Beverage. The result can be null.
     * @throws NoSuchElementException
     */
    Optional<Beverage> findById(Long id);

    /**
     * Updates existing beverage
     * 
     * @param id       beverage id
     * @param name     beverage name
     * @param price    price of beverage
     * @param currency
     * @return beverage
     * @throws ServiceException
     */
    Beverage save(Long id, String name, BigDecimal price, String currency) throws ServiceException;

    /**
     * Creaates beverage
     * 
     * @param id       beverage id
     * @param name     beverage name
     * @param price    price of beverage
     * @param currency
     * @return
     * @throws ServiceException
     */
    Beverage save(String name, BigDecimal price, String currency) throws ServiceException;

    /**
     * Deletes beverage
     * 
     * @param id beverage id
     * @throws ServiceException
     */
    void delete(Long id) throws ServiceException;

}
