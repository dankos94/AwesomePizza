package it.adesso.awesomepizza.domain;

import static it.adesso.awesomepizza.domain.PizzaMenuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PizzaMenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PizzaMenu.class);
        PizzaMenu pizzaMenu1 = getPizzaMenuSample1();
        PizzaMenu pizzaMenu2 = new PizzaMenu();
        assertThat(pizzaMenu1).isNotEqualTo(pizzaMenu2);

        pizzaMenu2.setId(pizzaMenu1.getId());
        assertThat(pizzaMenu1).isEqualTo(pizzaMenu2);

        pizzaMenu2 = getPizzaMenuSample2();
        assertThat(pizzaMenu1).isNotEqualTo(pizzaMenu2);
    }
}
