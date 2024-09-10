package it.adesso.awesomepizza.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentStatusDTO.class);
        PaymentStatusDTO paymentStatusDTO1 = new PaymentStatusDTO();
        paymentStatusDTO1.setId(1L);
        PaymentStatusDTO paymentStatusDTO2 = new PaymentStatusDTO();
        assertThat(paymentStatusDTO1).isNotEqualTo(paymentStatusDTO2);
        paymentStatusDTO2.setId(paymentStatusDTO1.getId());
        assertThat(paymentStatusDTO1).isEqualTo(paymentStatusDTO2);
        paymentStatusDTO2.setId(2L);
        assertThat(paymentStatusDTO1).isNotEqualTo(paymentStatusDTO2);
        paymentStatusDTO1.setId(null);
        assertThat(paymentStatusDTO1).isNotEqualTo(paymentStatusDTO2);
    }
}
