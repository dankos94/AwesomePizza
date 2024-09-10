package it.adesso.awesomepizza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Dish.
 */
@Entity
@Table(name = "dish")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pizzaMenu" }, allowSetters = true)
    private MenuSection menuSection;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "dishes")
    @JsonIgnoreProperties(value = { "payment", "user", "orderStatus", "dishes" }, allowSetters = true)
    private Set<PizzaOrder> pizzaOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dish id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Dish name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Dish description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Dish price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return this.available;
    }

    public Dish available(Boolean available) {
        this.setAvailable(available);
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public MenuSection getMenuSection() {
        return this.menuSection;
    }

    public void setMenuSection(MenuSection menuSection) {
        this.menuSection = menuSection;
    }

    public Dish menuSection(MenuSection menuSection) {
        this.setMenuSection(menuSection);
        return this;
    }

    public Set<PizzaOrder> getPizzaOrders() {
        return this.pizzaOrders;
    }

    public void setPizzaOrders(Set<PizzaOrder> pizzaOrders) {
        if (this.pizzaOrders != null) {
            this.pizzaOrders.forEach(i -> i.removeDish(this));
        }
        if (pizzaOrders != null) {
            pizzaOrders.forEach(i -> i.addDish(this));
        }
        this.pizzaOrders = pizzaOrders;
    }

    public Dish pizzaOrders(Set<PizzaOrder> pizzaOrders) {
        this.setPizzaOrders(pizzaOrders);
        return this;
    }

    public Dish addPizzaOrder(PizzaOrder pizzaOrder) {
        this.pizzaOrders.add(pizzaOrder);
        pizzaOrder.getDishes().add(this);
        return this;
    }

    public Dish removePizzaOrder(PizzaOrder pizzaOrder) {
        this.pizzaOrders.remove(pizzaOrder);
        pizzaOrder.getDishes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dish)) {
            return false;
        }
        return getId() != null && getId().equals(((Dish) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dish{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", available='" + getAvailable() + "'" +
            "}";
    }
}
