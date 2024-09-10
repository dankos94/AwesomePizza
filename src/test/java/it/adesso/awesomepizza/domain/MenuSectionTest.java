package it.adesso.awesomepizza.domain;

import static it.adesso.awesomepizza.domain.MenuSectionTestSamples.*;
import static it.adesso.awesomepizza.domain.PizzaMenuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuSectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuSection.class);
        MenuSection menuSection1 = getMenuSectionSample1();
        MenuSection menuSection2 = new MenuSection();
        assertThat(menuSection1).isNotEqualTo(menuSection2);

        menuSection2.setId(menuSection1.getId());
        assertThat(menuSection1).isEqualTo(menuSection2);

        menuSection2 = getMenuSectionSample2();
        assertThat(menuSection1).isNotEqualTo(menuSection2);
    }

    @Test
    void pizzaMenuTest() {
        MenuSection menuSection = getMenuSectionRandomSampleGenerator();
        PizzaMenu pizzaMenuBack = getPizzaMenuRandomSampleGenerator();

        menuSection.setPizzaMenu(pizzaMenuBack);
        assertThat(menuSection.getPizzaMenu()).isEqualTo(pizzaMenuBack);

        menuSection.pizzaMenu(null);
        assertThat(menuSection.getPizzaMenu()).isNull();
    }
}
