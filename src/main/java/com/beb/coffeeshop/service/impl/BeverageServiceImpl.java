package com.beb.coffeeshop.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.model.Currency;
import com.beb.coffeeshop.repository.BeverageRepository;
import com.beb.coffeeshop.service.BeverageService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.lang.Assert;

public class BeverageServiceImpl implements BeverageService {

    private BeverageRepository beverageRepository;

    public BeverageServiceImpl(BeverageRepository beverageRepository) {
        this.beverageRepository = beverageRepository;
    }

    @Override
    public List<Beverage> getAll() {
        return beverageRepository.findAll();
    }

    @Override
    public Page<Beverage> getAll(Integer pageNo, Integer pageSize, String sortColumn) {
        Assert.notNull(pageNo, "pageNo can not be null");
        Assert.notNull(pageSize, "pageSize can not be null");
        Assert.hasText(sortColumn, "sortColumn can not be null");

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn));
        return beverageRepository.findAll(pageable);
    }

    @Override
    public Optional<Beverage> findById(Long id) {
        Assert.notNull(id, "id can not be null");

        return beverageRepository.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Beverage save(Long id, String name, BigDecimal price, String currency) throws ServiceException {
        Assert.notNull(id, "id can not be null");
        Assert.notNull(price, "price can not be null");
        Assert.hasText(name, "name can not be null");
        Assert.hasText(currency, "currency can not be null");

        Beverage beverage = beverageRepository.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Beverage not found by id: %d", id)));
        Currency curencyType;
        try {
            curencyType = Currency.valueOf(currency);
        } catch (Exception e) {
            throw new ServiceException("Incompatible currentcy type");
        }

        beverage.setName(name);
        beverage.setPrice(price);
        beverage.setCurrency(curencyType);
        return beverageRepository.save(beverage);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Beverage save(String name, BigDecimal price, String currency) throws ServiceException {
        Assert.hasText(name, "name can not be null");
        Assert.notNull(price, "price can not be null");
        Assert.hasText(currency, "currency can not be null");

        Currency curencyType;
        try {
            curencyType = Currency.valueOf(currency);
        } catch (Exception e) {
            throw new ServiceException("Incompatible currentcy type");
        }
        Beverage beverage = new Beverage();
        beverage.setName(name);
        beverage.setPrice(price);
        beverage.setCurrency(curencyType);
        return beverageRepository.save(beverage);
    }

    @Override
    public void delete(Long id) throws ServiceException {
        Assert.notNull(id, "id can not be null");

        try {
            beverageRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error while deleting beverage", e);
        }
    }

}
