package it.adesso.awesomepizza.service.mapper;

import static it.adesso.awesomepizza.domain.PizzaMenuAsserts.*;
import static it.adesso.awesomepizza.domain.PizzaMenuTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PizzaMenuMapperTest {

    private PizzaMenuMapper pizzaMenuMapper;

    @BeforeEach
    void setUp() {
        pizzaMenuMapper = new PizzaMenuMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPizzaMenuSample1();
        var actual = pizzaMenuMapper.toEntity(pizzaMenuMapper.toDto(expected));
        assertPizzaMenuAllPropertiesEquals(expected, actual);
    }
}
