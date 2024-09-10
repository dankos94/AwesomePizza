package it.adesso.awesomepizza.domain;

import static it.adesso.awesomepizza.domain.DishTestSamples.*;
import static it.adesso.awesomepizza.domain.MenuSectionTestSamples.*;
import static it.adesso.awesomepizza.domain.PizzaOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DishTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dish.class);
        Dish dish1 = getDishSample1();
        Dish dish2 = new Dish();
        assertThat(dish1).isNotEqualTo(dish2);

        dish2.setId(dish1.getId());
        assertThat(dish1).isEqualTo(dish2);

        dish2 = getDishSample2();
        assertThat(dish1).isNotEqualTo(dish2);
    }

    @Test
    void menuSectionTest() {
        Dish dish = getDishRandomSampleGenerator();
        MenuSection menuSectionBack = getMenuSectionRandomSampleGenerator();

        dish.setMenuSection(menuSectionBack);
        assertThat(dish.getMenuSection()).isEqualTo(menuSectionBack);

        dish.menuSection(null);
        assertThat(dish.getMenuSection()).isNull();
    }

    @Test
    void pizzaOrderTest() {
        Dish dish = getDishRandomSampleGenerator();
        PizzaOrder pizzaOrderBack = getPizzaOrderRandomSampleGenerator();

        dish.addPizzaOrder(pizzaOrderBack);
        assertThat(dish.getPizzaOrders()).containsOnly(pizzaOrderBack);
        assertThat(pizzaOrderBack.getDishes()).containsOnly(dish);

        dish.removePizzaOrder(pizzaOrderBack);
        assertThat(dish.getPizzaOrders()).doesNotContain(pizzaOrderBack);
        assertThat(pizzaOrderBack.getDishes()).doesNotContain(dish);

        dish.pizzaOrders(new HashSet<>(Set.of(pizzaOrderBack)));
        assertThat(dish.getPizzaOrders()).containsOnly(pizzaOrderBack);
        assertThat(pizzaOrderBack.getDishes()).containsOnly(dish);

        dish.setPizzaOrders(new HashSet<>());
        assertThat(dish.getPizzaOrders()).doesNotContain(pizzaOrderBack);
        assertThat(pizzaOrderBack.getDishes()).doesNotContain(dish);
    }
}
