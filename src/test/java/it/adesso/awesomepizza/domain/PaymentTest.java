package it.adesso.awesomepizza.domain;

import static it.adesso.awesomepizza.domain.PaymentMethodTestSamples.*;
import static it.adesso.awesomepizza.domain.PaymentStatusTestSamples.*;
import static it.adesso.awesomepizza.domain.PaymentTestSamples.*;
import static it.adesso.awesomepizza.domain.PizzaOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void paymentMethodTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        payment.setPaymentMethod(paymentMethodBack);
        assertThat(payment.getPaymentMethod()).isEqualTo(paymentMethodBack);

        payment.paymentMethod(null);
        assertThat(payment.getPaymentMethod()).isNull();
    }

    @Test
    void paymentStatusTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentStatus paymentStatusBack = getPaymentStatusRandomSampleGenerator();

        payment.setPaymentStatus(paymentStatusBack);
        assertThat(payment.getPaymentStatus()).isEqualTo(paymentStatusBack);

        payment.paymentStatus(null);
        assertThat(payment.getPaymentStatus()).isNull();
    }

    @Test
    void pizzaOrderTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PizzaOrder pizzaOrderBack = getPizzaOrderRandomSampleGenerator();

        payment.setPizzaOrder(pizzaOrderBack);
        assertThat(payment.getPizzaOrder()).isEqualTo(pizzaOrderBack);
        assertThat(pizzaOrderBack.getPayment()).isEqualTo(payment);

        payment.pizzaOrder(null);
        assertThat(payment.getPizzaOrder()).isNull();
        assertThat(pizzaOrderBack.getPayment()).isNull();
    }
}
