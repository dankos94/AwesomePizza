package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.OrderStatus;
import it.adesso.awesomepizza.service.dto.OrderStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderStatus} and its DTO {@link OrderStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderStatusMapper extends EntityMapper<OrderStatusDTO, OrderStatus> {}
