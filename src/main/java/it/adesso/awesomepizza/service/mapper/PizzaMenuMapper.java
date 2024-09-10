package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.PizzaMenu;
import it.adesso.awesomepizza.domain.User;
import it.adesso.awesomepizza.service.dto.PizzaMenuDTO;
import it.adesso.awesomepizza.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PizzaMenu} and its DTO {@link PizzaMenuDTO}.
 */
@Mapper(componentModel = "spring")
public interface PizzaMenuMapper extends EntityMapper<PizzaMenuDTO, PizzaMenu> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    PizzaMenuDTO toDto(PizzaMenu s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
