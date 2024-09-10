package it.adesso.awesomepizza.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.adesso.awesomepizza.domain.MenuSection} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuSectionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private PizzaMenuDTO pizzaMenu;

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

    public PizzaMenuDTO getPizzaMenu() {
        return pizzaMenu;
    }

    public void setPizzaMenu(PizzaMenuDTO pizzaMenu) {
        this.pizzaMenu = pizzaMenu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuSectionDTO)) {
            return false;
        }

        MenuSectionDTO menuSectionDTO = (MenuSectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, menuSectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuSectionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", pizzaMenu=" + getPizzaMenu() +
            "}";
    }
}
