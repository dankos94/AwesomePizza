package it.adesso.awesomepizza.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PizzaMenuDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PizzaMenuDTO.class);
        PizzaMenuDTO pizzaMenuDTO1 = new PizzaMenuDTO();
        pizzaMenuDTO1.setId(1L);
        PizzaMenuDTO pizzaMenuDTO2 = new PizzaMenuDTO();
        assertThat(pizzaMenuDTO1).isNotEqualTo(pizzaMenuDTO2);
        pizzaMenuDTO2.setId(pizzaMenuDTO1.getId());
        assertThat(pizzaMenuDTO1).isEqualTo(pizzaMenuDTO2);
        pizzaMenuDTO2.setId(2L);
        assertThat(pizzaMenuDTO1).isNotEqualTo(pizzaMenuDTO2);
        pizzaMenuDTO1.setId(null);
        assertThat(pizzaMenuDTO1).isNotEqualTo(pizzaMenuDTO2);
    }
}
