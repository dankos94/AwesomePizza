package it.adesso.awesomepizza.domain;

import static it.adesso.awesomepizza.domain.PaymentStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentStatus.class);
        PaymentStatus paymentStatus1 = getPaymentStatusSample1();
        PaymentStatus paymentStatus2 = new PaymentStatus();
        assertThat(paymentStatus1).isNotEqualTo(paymentStatus2);

        paymentStatus2.setId(paymentStatus1.getId());
        assertThat(paymentStatus1).isEqualTo(paymentStatus2);

        paymentStatus2 = getPaymentStatusSample2();
        assertThat(paymentStatus1).isNotEqualTo(paymentStatus2);
    }
}
