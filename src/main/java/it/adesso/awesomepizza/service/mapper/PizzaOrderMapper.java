package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.Dish;
import it.adesso.awesomepizza.domain.OrderStatus;
import it.adesso.awesomepizza.domain.Payment;
import it.adesso.awesomepizza.domain.PizzaOrder;
import it.adesso.awesomepizza.domain.User;
import it.adesso.awesomepizza.service.dto.DishDTO;
import it.adesso.awesomepizza.service.dto.OrderStatusDTO;
import it.adesso.awesomepizza.service.dto.PaymentDTO;
import it.adesso.awesomepizza.service.dto.PizzaOrderDTO;
import it.adesso.awesomepizza.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PizzaOrder} and its DTO {@link PizzaOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PizzaOrderMapper extends EntityMapper<PizzaOrderDTO, PizzaOrder> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "orderStatus", source = "orderStatus", qualifiedByName = "orderStatusId")
    @Mapping(target = "dishes", source = "dishes", qualifiedByName = "dishIdSet")
    PizzaOrderDTO toDto(PizzaOrder s);

    @Mapping(target = "removeDish", ignore = true)
    PizzaOrder toEntity(PizzaOrderDTO pizzaOrderDTO);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("orderStatusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderStatusDTO toDtoOrderStatusId(OrderStatus orderStatus);

    @Named("dishId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DishDTO toDtoDishId(Dish dish);

    @Named("dishIdSet")
    default Set<DishDTO> toDtoDishIdSet(Set<Dish> dish) {
        return dish.stream().map(this::toDtoDishId).collect(Collectors.toSet());
    }
}
