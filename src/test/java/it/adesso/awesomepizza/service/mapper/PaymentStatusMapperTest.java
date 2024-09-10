package it.adesso.awesomepizza.service.mapper;

import static it.adesso.awesomepizza.domain.PaymentStatusAsserts.*;
import static it.adesso.awesomepizza.domain.PaymentStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentStatusMapperTest {

    private PaymentStatusMapper paymentStatusMapper;

    @BeforeEach
    void setUp() {
        paymentStatusMapper = new PaymentStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentStatusSample1();
        var actual = paymentStatusMapper.toEntity(paymentStatusMapper.toDto(expected));
        assertPaymentStatusAllPropertiesEquals(expected, actual);
    }
}
