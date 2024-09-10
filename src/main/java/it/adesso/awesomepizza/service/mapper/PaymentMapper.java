package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.Payment;
import it.adesso.awesomepizza.domain.PaymentMethod;
import it.adesso.awesomepizza.domain.PaymentStatus;
import it.adesso.awesomepizza.service.dto.PaymentDTO;
import it.adesso.awesomepizza.service.dto.PaymentMethodDTO;
import it.adesso.awesomepizza.service.dto.PaymentStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "paymentMethod", source = "paymentMethod", qualifiedByName = "paymentMethodId")
    @Mapping(target = "paymentStatus", source = "paymentStatus", qualifiedByName = "paymentStatusId")
    PaymentDTO toDto(Payment s);

    @Named("paymentMethodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMethodDTO toDtoPaymentMethodId(PaymentMethod paymentMethod);

    @Named("paymentStatusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentStatusDTO toDtoPaymentStatusId(PaymentStatus paymentStatus);
}
