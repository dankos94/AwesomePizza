package it.adesso.awesomepizza.web.rest;

import static it.adesso.awesomepizza.domain.PizzaMenuAsserts.*;
import static it.adesso.awesomepizza.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.adesso.awesomepizza.IntegrationTest;
import it.adesso.awesomepizza.domain.PizzaMenu;
import it.adesso.awesomepizza.repository.PizzaMenuRepository;
import it.adesso.awesomepizza.repository.UserRepository;
import it.adesso.awesomepizza.service.dto.PizzaMenuDTO;
import it.adesso.awesomepizza.service.mapper.PizzaMenuMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PizzaMenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PizzaMenuResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/pizza-menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PizzaMenuRepository pizzaMenuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PizzaMenuMapper pizzaMenuMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPizzaMenuMockMvc;

    private PizzaMenu pizzaMenu;

    private PizzaMenu insertedPizzaMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PizzaMenu createEntity() {
        return new PizzaMenu().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PizzaMenu createUpdatedEntity() {
        return new PizzaMenu().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        pizzaMenu = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPizzaMenu != null) {
            pizzaMenuRepository.delete(insertedPizzaMenu);
            insertedPizzaMenu = null;
        }
    }

    @Test
    @Transactional
    void createPizzaMenu() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PizzaMenu
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);
        var returnedPizzaMenuDTO = om.readValue(
            restPizzaMenuMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaMenuDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PizzaMenuDTO.class
        );

        // Validate the PizzaMenu in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPizzaMenu = pizzaMenuMapper.toEntity(returnedPizzaMenuDTO);
        assertPizzaMenuUpdatableFieldsEquals(returnedPizzaMenu, getPersistedPizzaMenu(returnedPizzaMenu));

        insertedPizzaMenu = returnedPizzaMenu;
    }

    @Test
    @Transactional
    void createPizzaMenuWithExistingId() throws Exception {
        // Create the PizzaMenu with an existing ID
        pizzaMenu.setId(1L);
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPizzaMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaMenuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pizzaMenu.setName(null);

        // Create the PizzaMenu, which fails.
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        restPizzaMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaMenuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pizzaMenu.setCreatedDate(null);

        // Create the PizzaMenu, which fails.
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        restPizzaMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaMenuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPizzaMenus() throws Exception {
        // Initialize the database
        insertedPizzaMenu = pizzaMenuRepository.saveAndFlush(pizzaMenu);

        // Get all the pizzaMenuList
        restPizzaMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pizzaMenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getPizzaMenu() throws Exception {
        // Initialize the database
        insertedPizzaMenu = pizzaMenuRepository.saveAndFlush(pizzaMenu);

        // Get the pizzaMenu
        restPizzaMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, pizzaMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pizzaMenu.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPizzaMenu() throws Exception {
        // Get the pizzaMenu
        restPizzaMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPizzaMenu() throws Exception {
        // Initialize the database
        insertedPizzaMenu = pizzaMenuRepository.saveAndFlush(pizzaMenu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pizzaMenu
        PizzaMenu updatedPizzaMenu = pizzaMenuRepository.findById(pizzaMenu.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPizzaMenu are not directly saved in db
        em.detach(updatedPizzaMenu);
        updatedPizzaMenu.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(updatedPizzaMenu);

        restPizzaMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pizzaMenuDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pizzaMenuDTO))
            )
            .andExpect(status().isOk());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPizzaMenuToMatchAllProperties(updatedPizzaMenu);
    }

    @Test
    @Transactional
    void putNonExistingPizzaMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaMenu.setId(longCount.incrementAndGet());

        // Create the PizzaMenu
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPizzaMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pizzaMenuDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pizzaMenuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPizzaMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaMenu.setId(longCount.incrementAndGet());

        // Create the PizzaMenu
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pizzaMenuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPizzaMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaMenu.setId(longCount.incrementAndGet());

        // Create the PizzaMenu
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaMenuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePizzaMenuWithPatch() throws Exception {
        // Initialize the database
        insertedPizzaMenu = pizzaMenuRepository.saveAndFlush(pizzaMenu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pizzaMenu using partial update
        PizzaMenu partialUpdatedPizzaMenu = new PizzaMenu();
        partialUpdatedPizzaMenu.setId(pizzaMenu.getId());

        partialUpdatedPizzaMenu.createdDate(UPDATED_CREATED_DATE);

        restPizzaMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPizzaMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPizzaMenu))
            )
            .andExpect(status().isOk());

        // Validate the PizzaMenu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPizzaMenuUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPizzaMenu, pizzaMenu),
            getPersistedPizzaMenu(pizzaMenu)
        );
    }

    @Test
    @Transactional
    void fullUpdatePizzaMenuWithPatch() throws Exception {
        // Initialize the database
        insertedPizzaMenu = pizzaMenuRepository.saveAndFlush(pizzaMenu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pizzaMenu using partial update
        PizzaMenu partialUpdatedPizzaMenu = new PizzaMenu();
        partialUpdatedPizzaMenu.setId(pizzaMenu.getId());

        partialUpdatedPizzaMenu.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);

        restPizzaMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPizzaMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPizzaMenu))
            )
            .andExpect(status().isOk());

        // Validate the PizzaMenu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPizzaMenuUpdatableFieldsEquals(partialUpdatedPizzaMenu, getPersistedPizzaMenu(partialUpdatedPizzaMenu));
    }

    @Test
    @Transactional
    void patchNonExistingPizzaMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaMenu.setId(longCount.incrementAndGet());

        // Create the PizzaMenu
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPizzaMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pizzaMenuDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pizzaMenuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPizzaMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaMenu.setId(longCount.incrementAndGet());

        // Create the PizzaMenu
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pizzaMenuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPizzaMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaMenu.setId(longCount.incrementAndGet());

        // Create the PizzaMenu
        PizzaMenuDTO pizzaMenuDTO = pizzaMenuMapper.toDto(pizzaMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaMenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pizzaMenuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PizzaMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePizzaMenu() throws Exception {
        // Initialize the database
        insertedPizzaMenu = pizzaMenuRepository.saveAndFlush(pizzaMenu);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pizzaMenu
        restPizzaMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, pizzaMenu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pizzaMenuRepository.count();
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

    protected PizzaMenu getPersistedPizzaMenu(PizzaMenu pizzaMenu) {
        return pizzaMenuRepository.findById(pizzaMenu.getId()).orElseThrow();
    }

    protected void assertPersistedPizzaMenuToMatchAllProperties(PizzaMenu expectedPizzaMenu) {
        assertPizzaMenuAllPropertiesEquals(expectedPizzaMenu, getPersistedPizzaMenu(expectedPizzaMenu));
    }

    protected void assertPersistedPizzaMenuToMatchUpdatableProperties(PizzaMenu expectedPizzaMenu) {
        assertPizzaMenuAllUpdatablePropertiesEquals(expectedPizzaMenu, getPersistedPizzaMenu(expectedPizzaMenu));
    }
}
