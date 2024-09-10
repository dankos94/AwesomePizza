package it.adesso.awesomepizza.domain;

import static it.adesso.awesomepizza.domain.OrderStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderStatus.class);
        OrderStatus orderStatus1 = getOrderStatusSample1();
        OrderStatus orderStatus2 = new OrderStatus();
        assertThat(orderStatus1).isNotEqualTo(orderStatus2);

        orderStatus2.setId(orderStatus1.getId());
        assertThat(orderStatus1).isEqualTo(orderStatus2);

        orderStatus2 = getOrderStatusSample2();
        assertThat(orderStatus1).isNotEqualTo(orderStatus2);
    }
}
