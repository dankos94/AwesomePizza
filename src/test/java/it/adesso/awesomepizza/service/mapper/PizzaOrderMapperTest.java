package it.adesso.awesomepizza.service.mapper;

import static it.adesso.awesomepizza.domain.PizzaOrderAsserts.*;
import static it.adesso.awesomepizza.domain.PizzaOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PizzaOrderMapperTest {

    private PizzaOrderMapper pizzaOrderMapper;

    @BeforeEach
    void setUp() {
        pizzaOrderMapper = new PizzaOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPizzaOrderSample1();
        var actual = pizzaOrderMapper.toEntity(pizzaOrderMapper.toDto(expected));
        assertPizzaOrderAllPropertiesEquals(expected, actual);
    }
}
