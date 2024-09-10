package it.adesso.awesomepizza.service.mapper;

import it.adesso.awesomepizza.domain.PaymentStatus;
import it.adesso.awesomepizza.service.dto.PaymentStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentStatus} and its DTO {@link PaymentStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentStatusMapper extends EntityMapper<PaymentStatusDTO, PaymentStatus> {}
