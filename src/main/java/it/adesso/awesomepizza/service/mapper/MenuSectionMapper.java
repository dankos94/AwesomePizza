package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.MenuSection;
import it.adesso.awesomepizza.domain.PizzaMenu;
import it.adesso.awesomepizza.service.dto.MenuSectionDTO;
import it.adesso.awesomepizza.service.dto.PizzaMenuDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuSection} and its DTO {@link MenuSectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuSectionMapper extends EntityMapper<MenuSectionDTO, MenuSection> {
    @Mapping(target = "pizzaMenu", source = "pizzaMenu", qualifiedByName = "pizzaMenuId")
    MenuSectionDTO toDto(MenuSection s);

    @Named("pizzaMenuId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PizzaMenuDTO toDtoPizzaMenuId(PizzaMenu pizzaMenu);
}
