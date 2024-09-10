package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.PaymentMethod;
import it.adesso.awesomepizza.service.dto.PaymentMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {}
