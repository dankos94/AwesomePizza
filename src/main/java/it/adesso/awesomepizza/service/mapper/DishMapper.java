package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.Dish;
import it.adesso.awesomepizza.domain.MenuSection;
import it.adesso.awesomepizza.domain.PizzaOrder;
import it.adesso.awesomepizza.service.dto.DishDTO;
import it.adesso.awesomepizza.service.dto.MenuSectionDTO;
import it.adesso.awesomepizza.service.dto.PizzaOrderDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dish} and its DTO {@link DishDTO}.
 */
@Mapper(componentModel = "spring")
public interface DishMapper extends EntityMapper<DishDTO, Dish> {
    @Mapping(target = "menuSection", source = "menuSection", qualifiedByName = "menuSectionId")
    @Mapping(target = "pizzaOrders", source = "pizzaOrders", qualifiedByName = "pizzaOrderIdSet")
    DishDTO toDto(Dish s);

    @Mapping(target = "pizzaOrders", ignore = true)
    @Mapping(target = "removePizzaOrder", ignore = true)
    Dish toEntity(DishDTO dishDTO);

    @Named("menuSectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuSectionDTO toDtoMenuSectionId(MenuSection menuSection);

    @Named("pizzaOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PizzaOrderDTO toDtoPizzaOrderId(PizzaOrder pizzaOrder);

    @Named("pizzaOrderIdSet")
    default Set<PizzaOrderDTO> toDtoPizzaOrderIdSet(Set<PizzaOrder> pizzaOrder) {
        return pizzaOrder.stream().map(this::toDtoPizzaOrderId).collect(Collectors.toSet());
    }
}
