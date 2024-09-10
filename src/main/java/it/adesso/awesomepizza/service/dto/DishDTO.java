package it.adesso.awesomepizza.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link it.adesso.awesomepizza.domain.Dish} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DishDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Boolean available;

    private MenuSectionDTO menuSection;

    private Set<PizzaOrderDTO> pizzaOrders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public MenuSectionDTO getMenuSection() {
        return menuSection;
    }

    public void setMenuSection(MenuSectionDTO menuSection) {
        this.menuSection = menuSection;
    }

    public Set<PizzaOrderDTO> getPizzaOrders() {
        return pizzaOrders;
    }

    public void setPizzaOrders(Set<PizzaOrderDTO> pizzaOrders) {
        this.pizzaOrders = pizzaOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DishDTO)) {
            return false;
        }

        DishDTO dishDTO = (DishDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dishDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DishDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", available='" + getAvailable() + "'" +
            ", menuSection=" + getMenuSection() +
            ", pizzaOrders=" + getPizzaOrders() +
            "}";
    }
}
