package it.adesso.awesomepizza.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.adesso.awesomepizza.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuSectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuSectionDTO.class);
        MenuSectionDTO menuSectionDTO1 = new MenuSectionDTO();
        menuSectionDTO1.setId(1L);
        MenuSectionDTO menuSectionDTO2 = new MenuSectionDTO();
        assertThat(menuSectionDTO1).isNotEqualTo(menuSectionDTO2);
        menuSectionDTO2.setId(menuSectionDTO1.getId());
        assertThat(menuSectionDTO1).isEqualTo(menuSectionDTO2);
        menuSectionDTO2.setId(2L);
        assertThat(menuSectionDTO1).isNotEqualTo(menuSectionDTO2);
        menuSectionDTO1.setId(null);
        assertThat(menuSectionDTO1).isNotEqualTo(menuSectionDTO2);
    }
}
