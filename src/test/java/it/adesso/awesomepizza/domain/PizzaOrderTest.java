package it.adesso.awesomepizza.domain;

import static it.adesso.awesomepizza.domain.DishTestSamples.*;
import static it.adesso.awesomepizza.domain.OrderStatusTestSamples.*;
import static it.adesso.awesomepizza.domain.PaymentTestSamples.*;
import static it.adesso.awesomepizza.domain.PizzaOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PizzaOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PizzaOrder.class);
        PizzaOrder pizzaOrder1 = getPizzaOrderSample1();
        PizzaOrder pizzaOrder2 = new PizzaOrder();
        assertThat(pizzaOrder1).isNotEqualTo(pizzaOrder2);

        pizzaOrder2.setId(pizzaOrder1.getId());
        assertThat(pizzaOrder1).isEqualTo(pizzaOrder2);

        pizzaOrder2 = getPizzaOrderSample2();
        assertThat(pizzaOrder1).isNotEqualTo(pizzaOrder2);
    }

    @Test
    void paymentTest() {
        PizzaOrder pizzaOrder = getPizzaOrderRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        pizzaOrder.setPayment(paymentBack);
        assertThat(pizzaOrder.getPayment()).isEqualTo(paymentBack);

        pizzaOrder.payment(null);
        assertThat(pizzaOrder.getPayment()).isNull();
    }

    @Test
    void orderStatusTest() {
        PizzaOrder pizzaOrder = getPizzaOrderRandomSampleGenerator();
        OrderStatus orderStatusBack = getOrderStatusRandomSampleGenerator();

        pizzaOrder.setOrderStatus(orderStatusBack);
        assertThat(pizzaOrder.getOrderStatus()).isEqualTo(orderStatusBack);

        pizzaOrder.orderStatus(null);
        assertThat(pizzaOrder.getOrderStatus()).isNull();
    }

    @Test
    void dishTest() {
        PizzaOrder pizzaOrder = getPizzaOrderRandomSampleGenerator();
        Dish dishBack = getDishRandomSampleGenerator();

        pizzaOrder.addDish(dishBack);
        assertThat(pizzaOrder.getDishes()).containsOnly(dishBack);

        pizzaOrder.removeDish(dishBack);
        assertThat(pizzaOrder.getDishes()).doesNotContain(dishBack);

        pizzaOrder.dishes(new HashSet<>(Set.of(dishBack)));
        assertThat(pizzaOrder.getDishes()).containsOnly(dishBack);

        pizzaOrder.setDishes(new HashSet<>());
        assertThat(pizzaOrder.getDishes()).doesNotContain(dishBack);
    }
}
