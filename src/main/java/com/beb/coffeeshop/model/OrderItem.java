package com.beb.coffeeshop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * OrderItem entity. Holds the beverage and toppings attached to it.
 */
@Entity
@Table(name = "orderitems")
public class OrderItem extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "orderitem_sequence", initialValue = 1, allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false)
    private Order order;

    @ManyToOne(optional = false)
    private Beverage beverage;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "orderitem_toppings", joinColumns = @JoinColumn(name = "orderitem_id"), inverseJoinColumns = @JoinColumn(name = "topping_id"))
    private Set<Topping> toppings = new HashSet<>();

    /**
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * @return Beverage
     */
    public Beverage getBeverage() {
        return beverage;
    }

    /**
     * @param beverage
     */
    public void setBeverage(Beverage beverage) {
        this.beverage = beverage;
    }

    /**
     * @return Set<Topping>
     */
    public Set<Topping> getToppings() {
        return toppings;
    }

    /**
     * @param toppings
     */
    public void setToppings(Set<Topping> toppings) {
        this.toppings = toppings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderItem other = (OrderItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

}
