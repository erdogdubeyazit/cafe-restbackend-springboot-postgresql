package com.beb.coffeeshop.repository;

import java.util.Optional;

import com.beb.coffeeshop.model.Topping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, Long> {

    /**
     * Search toppings by name
     * 
     * @param name name of the beverage
     * @return Topping. Consider result can be null.
     */
    Optional<Topping> findByName(String name);

}
