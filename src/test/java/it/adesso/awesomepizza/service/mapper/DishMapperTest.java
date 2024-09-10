package it.adesso.awesomepizza.service.mapper;

import static it.adesso.awesomepizza.domain.DishAsserts.*;
import static it.adesso.awesomepizza.domain.DishTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DishMapperTest {

    private DishMapper dishMapper;

    @BeforeEach
    void setUp() {
        dishMapper = new DishMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDishSample1();
        var actual = dishMapper.toEntity(dishMapper.toDto(expected));
        assertDishAllPropertiesEquals(expected, actual);
    }
}
