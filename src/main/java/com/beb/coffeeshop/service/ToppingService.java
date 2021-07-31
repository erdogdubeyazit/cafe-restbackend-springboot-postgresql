package com.beb.coffeeshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Topping;

import org.springframework.data.domain.Page;

/**
 * Service for topping operations.
 * 
 * @author Beyazit
 * @category Service
 */
public interface ToppingService {

    /**
     * 
     * @return list of toppings
     */
    List<Topping> getAll();

    /**
     * @param pageNo     page that is requested to work on
     * @param pageSize   item count per page
     * @param sortColumn natural ordering by specific column
     * @return Pageable list of toppings
     */
    Page<Topping> getAll(Integer pageNo, Integer pageSize, String sortColumn);

    /**
     * 
     * @param id topping id
     * @return Topping. The result can be null.
     * @throws NoSuchElementException
     */
    Optional<Topping> findById(Long id);

    /**
     * Updates existing topping
     * 
     * @param id       topping id
     * @param name     topping name
     * @param price    price of topping
     * @param currency
     * @return topping
     * @throws ServiceException
     */
    Topping save(Long id, String name, BigDecimal price, String currency) throws ServiceException;

    /**
     * Creaates topping
     * 
     * @param id       topping id
     * @param name     topping name
     * @param price    price of topping
     * @param currency
     * @return
     * @throws ServiceException
     */
    Topping save(String name, BigDecimal price, String currency) throws ServiceException;

    /**
     * Deletes topping
     * 
     * @param id topping id
     * @throws ServiceException
     */
    void delete(Long id) throws ServiceException;

}
