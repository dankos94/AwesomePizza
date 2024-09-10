package it.adesso.awesomepizza.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PizzaOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PizzaOrderDTO.class);
        PizzaOrderDTO pizzaOrderDTO1 = new PizzaOrderDTO();
        pizzaOrderDTO1.setId(1L);
        PizzaOrderDTO pizzaOrderDTO2 = new PizzaOrderDTO();
        assertThat(pizzaOrderDTO1).isNotEqualTo(pizzaOrderDTO2);
        pizzaOrderDTO2.setId(pizzaOrderDTO1.getId());
        assertThat(pizzaOrderDTO1).isEqualTo(pizzaOrderDTO2);
        pizzaOrderDTO2.setId(2L);
        assertThat(pizzaOrderDTO1).isNotEqualTo(pizzaOrderDTO2);
        pizzaOrderDTO1.setId(null);
        assertThat(pizzaOrderDTO1).isNotEqualTo(pizzaOrderDTO2);
    }
}
