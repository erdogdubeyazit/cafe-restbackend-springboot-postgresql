package com.beb.coffeeshop.repository;

import java.util.Optional;

import com.beb.coffeeshop.model.Beverage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeverageRepository extends JpaRepository<Beverage, Long> {

    /**
     * Search beverags by name
     * 
     * @param name name of the beverage
     * @return Beverage. Consider result can be null.
     */
    Optional<Beverage> findByName(String name);
}
