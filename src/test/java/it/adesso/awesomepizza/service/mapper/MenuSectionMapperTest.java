package it.adesso.awesomepizza.service.mapper;

import static it.adesso.awesomepizza.domain.MenuSectionAsserts.*;
import static it.adesso.awesomepizza.domain.MenuSectionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuSectionMapperTest {

    private MenuSectionMapper menuSectionMapper;

    @BeforeEach
    void setUp() {
        menuSectionMapper = new MenuSectionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMenuSectionSample1();
        var actual = menuSectionMapper.toEntity(menuSectionMapper.toDto(expected));
        assertMenuSectionAllPropertiesEquals(expected, actual);
    }
}
