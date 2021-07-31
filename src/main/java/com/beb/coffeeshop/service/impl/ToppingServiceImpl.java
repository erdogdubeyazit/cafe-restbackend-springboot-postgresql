package com.beb.coffeeshop.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Currency;
import com.beb.coffeeshop.model.Topping;
import com.beb.coffeeshop.repository.ToppingRepository;
import com.beb.coffeeshop.service.ToppingService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class ToppingServiceImpl implements ToppingService {

    private ToppingRepository toppingRepository;

    public ToppingServiceImpl(ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    @Override
    public List<Topping> getAll() {
        return toppingRepository.findAll();
    }

    @Override
    public Page<Topping> getAll(Integer pageNo, Integer pageSize, String sortColumn) {
        Assert.notNull(pageNo, "pageNo can not be null");
        Assert.notNull(pageSize, "pageSize can not be null");
        Assert.hasText(sortColumn, "sortColumn can not be null");

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn));

        return toppingRepository.findAll(pageable);
    }

    @Override
    public Optional<Topping> findById(Long id) {
        Assert.notNull(id, "id can not be null");

        return toppingRepository.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Topping save(Long id, String name, BigDecimal price, String currency) throws ServiceException {
        Assert.notNull(id, "id can not be null");
        Assert.notNull(price, "price can not be null");
        Assert.hasText(name, "name can not be null");
        Assert.hasText(currency, "currency can not be null");

        Topping topping = toppingRepository.findById(id)
                .orElseThrow(() -> new ServiceException(String.format("Topping not found by id: %d", id)));

        Currency curencyType;
        try {
            curencyType = Currency.valueOf(currency);
        } catch (Exception e) {
            throw new ServiceException("Incompatible currentcy type");
        }

        topping.setName(name);
        topping.setPrice(price);
        topping.setCurrency(curencyType);

        return toppingRepository.save(topping);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Topping save(String name, BigDecimal price, String currency) throws ServiceException {
        Assert.notNull(price, "price can not be null");
        Assert.hasText(name, "name can not be null");
        Assert.hasText(currency, "currency can not be null");

        Topping topping = new Topping();

        Currency curencyType;
        try {
            curencyType = Currency.valueOf(currency);
        } catch (Exception e) {
            throw new ServiceException("Incompatible currentcy type");
        }

        topping.setName(name);
        topping.setPrice(price);
        topping.setCurrency(curencyType);

        return toppingRepository.save(topping);
    }

    @Override
    public void delete(Long id) throws ServiceException {
        Assert.notNull(id, "id can not be null");

        try {
            toppingRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error while deleting topping", e);
        }

    }

}
