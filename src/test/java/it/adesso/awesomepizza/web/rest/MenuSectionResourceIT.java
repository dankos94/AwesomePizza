package it.adesso.awesomepizza.web.rest;

import static it.adesso.awesomepizza.domain.MenuSectionAsserts.*;
import static it.adesso.awesomepizza.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.adesso.awesomepizza.IntegrationTest;
import it.adesso.awesomepizza.domain.MenuSection;
import it.adesso.awesomepizza.repository.MenuSectionRepository;
import it.adesso.awesomepizza.service.dto.MenuSectionDTO;
import it.adesso.awesomepizza.service.mapper.MenuSectionMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MenuSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuSectionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/menu-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MenuSectionRepository menuSectionRepository;

    @Autowired
    private MenuSectionMapper menuSectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuSectionMockMvc;

    private MenuSection menuSection;

    private MenuSection insertedMenuSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuSection createEntity() {
        return new MenuSection().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuSection createUpdatedEntity() {
        return new MenuSection().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        menuSection = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMenuSection != null) {
            menuSectionRepository.delete(insertedMenuSection);
            insertedMenuSection = null;
        }
    }

    @Test
    @Transactional
    void createMenuSection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MenuSection
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);
        var returnedMenuSectionDTO = om.readValue(
            restMenuSectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuSectionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MenuSectionDTO.class
        );

        // Validate the MenuSection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMenuSection = menuSectionMapper.toEntity(returnedMenuSectionDTO);
        assertMenuSectionUpdatableFieldsEquals(returnedMenuSection, getPersistedMenuSection(returnedMenuSection));

        insertedMenuSection = returnedMenuSection;
    }

    @Test
    @Transactional
    void createMenuSectionWithExistingId() throws Exception {
        // Create the MenuSection with an existing ID
        menuSection.setId(1L);
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuSectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuSection.setName(null);

        // Create the MenuSection, which fails.
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        restMenuSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuSectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenuSections() throws Exception {
        // Initialize the database
        insertedMenuSection = menuSectionRepository.saveAndFlush(menuSection);

        // Get all the menuSectionList
        restMenuSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMenuSection() throws Exception {
        // Initialize the database
        insertedMenuSection = menuSectionRepository.saveAndFlush(menuSection);

        // Get the menuSection
        restMenuSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, menuSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuSection.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMenuSection() throws Exception {
        // Get the menuSection
        restMenuSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuSection() throws Exception {
        // Initialize the database
        insertedMenuSection = menuSectionRepository.saveAndFlush(menuSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuSection
        MenuSection updatedMenuSection = menuSectionRepository.findById(menuSection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMenuSection are not directly saved in db
        em.detach(updatedMenuSection);
        updatedMenuSection.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(updatedMenuSection);

        restMenuSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuSectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuSectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMenuSectionToMatchAllProperties(updatedMenuSection);
    }

    @Test
    @Transactional
    void putNonExistingMenuSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuSection.setId(longCount.incrementAndGet());

        // Create the MenuSection
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuSectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuSection.setId(longCount.incrementAndGet());

        // Create the MenuSection
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuSection.setId(longCount.incrementAndGet());

        // Create the MenuSection
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuSectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuSectionWithPatch() throws Exception {
        // Initialize the database
        insertedMenuSection = menuSectionRepository.saveAndFlush(menuSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuSection using partial update
        MenuSection partialUpdatedMenuSection = new MenuSection();
        partialUpdatedMenuSection.setId(menuSection.getId());

        partialUpdatedMenuSection.description(UPDATED_DESCRIPTION);

        restMenuSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuSection))
            )
            .andExpect(status().isOk());

        // Validate the MenuSection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuSectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMenuSection, menuSection),
            getPersistedMenuSection(menuSection)
        );
    }

    @Test
    @Transactional
    void fullUpdateMenuSectionWithPatch() throws Exception {
        // Initialize the database
        insertedMenuSection = menuSectionRepository.saveAndFlush(menuSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuSection using partial update
        MenuSection partialUpdatedMenuSection = new MenuSection();
        partialUpdatedMenuSection.setId(menuSection.getId());

        partialUpdatedMenuSection.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMenuSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuSection))
            )
            .andExpect(status().isOk());

        // Validate the MenuSection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuSectionUpdatableFieldsEquals(partialUpdatedMenuSection, getPersistedMenuSection(partialUpdatedMenuSection));
    }

    @Test
    @Transactional
    void patchNonExistingMenuSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuSection.setId(longCount.incrementAndGet());

        // Create the MenuSection
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuSectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuSection.setId(longCount.incrementAndGet());

        // Create the MenuSection
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuSection.setId(longCount.incrementAndGet());

        // Create the MenuSection
        MenuSectionDTO menuSectionDTO = menuSectionMapper.toDto(menuSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(menuSectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuSection() throws Exception {
        // Initialize the database
        insertedMenuSection = menuSectionRepository.saveAndFlush(menuSection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the menuSection
        restMenuSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return menuSectionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MenuSection getPersistedMenuSection(MenuSection menuSection) {
        return menuSectionRepository.findById(menuSection.getId()).orElseThrow();
    }

    protected void assertPersistedMenuSectionToMatchAllProperties(MenuSection expectedMenuSection) {
        assertMenuSectionAllPropertiesEquals(expectedMenuSection, getPersistedMenuSection(expectedMenuSection));
    }

    protected void assertPersistedMenuSectionToMatchUpdatableProperties(MenuSection expectedMenuSection) {
        assertMenuSectionAllUpdatablePropertiesEquals(expectedMenuSection, getPersistedMenuSection(expectedMenuSection));
    }
}
