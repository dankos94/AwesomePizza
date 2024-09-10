package it.adesso.awesomepizza.web.rest;

import static it.adesso.awesomepizza.domain.DishAsserts.*;
import static it.adesso.awesomepizza.web.rest.TestUtil.createUpdateProxyForBean;
import static it.adesso.awesomepizza.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.adesso.awesomepizza.IntegrationTest;
import it.adesso.awesomepizza.domain.Dish;
import it.adesso.awesomepizza.repository.DishRepository;
import it.adesso.awesomepizza.service.dto.DishDTO;
import it.adesso.awesomepizza.service.mapper.DishMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link DishResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DishResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final String ENTITY_API_URL = "/api/dishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDishMockMvc;

    private Dish dish;

    private Dish insertedDish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createEntity() {
        return new Dish().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).price(DEFAULT_PRICE).available(DEFAULT_AVAILABLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createUpdatedEntity() {
        return new Dish().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE).available(UPDATED_AVAILABLE);
    }

    @BeforeEach
    public void initTest() {
        dish = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDish != null) {
            dishRepository.delete(insertedDish);
            insertedDish = null;
        }
    }

    @Test
    @Transactional
    void createDish() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);
        var returnedDishDTO = om.readValue(
            restDishMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DishDTO.class
        );

        // Validate the Dish in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDish = dishMapper.toEntity(returnedDishDTO);
        assertDishUpdatableFieldsEquals(returnedDish, getPersistedDish(returnedDish));

        insertedDish = returnedDish;
    }

    @Test
    @Transactional
    void createDishWithExistingId() throws Exception {
        // Create the Dish with an existing ID
        dish.setId(1L);
        DishDTO dishDTO = dishMapper.toDto(dish);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dish.setName(null);

        // Create the Dish, which fails.
        DishDTO dishDTO = dishMapper.toDto(dish);

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dish.setPrice(null);

        // Create the Dish, which fails.
        DishDTO dishDTO = dishMapper.toDto(dish);

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dish.setAvailable(null);

        // Create the Dish, which fails.
        DishDTO dishDTO = dishMapper.toDto(dish);

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDishes() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        // Get all the dishList
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getDish() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        // Get the dish
        restDishMockMvc
            .perform(get(ENTITY_API_URL_ID, dish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dish.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDish() throws Exception {
        // Get the dish
        restDishMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDish() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dish
        Dish updatedDish = dishRepository.findById(dish.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDish are not directly saved in db
        em.detach(updatedDish);
        updatedDish.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE).available(UPDATED_AVAILABLE);
        DishDTO dishDTO = dishMapper.toDto(updatedDish);

        restDishMockMvc
            .perform(put(ENTITY_API_URL_ID, dishDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isOk());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDishToMatchAllProperties(updatedDish);
    }

    @Test
    @Transactional
    void putNonExistingDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(put(ENTITY_API_URL_ID, dishDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dishDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDishWithPatch() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDishUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDish, dish), getPersistedDish(dish));
    }

    @Test
    @Transactional
    void fullUpdateDishWithPatch() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        partialUpdatedDish.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE).available(UPDATED_AVAILABLE);

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDishUpdatableFieldsEquals(partialUpdatedDish, getPersistedDish(partialUpdatedDish));
    }

    @Test
    @Transactional
    void patchNonExistingDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dishDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dishDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dishDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDish() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dish.setId(longCount.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dishDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDish() throws Exception {
        // Initialize the database
        insertedDish = dishRepository.saveAndFlush(dish);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dish
        restDishMockMvc
            .perform(delete(ENTITY_API_URL_ID, dish.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dishRepository.count();
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

    protected Dish getPersistedDish(Dish dish) {
        return dishRepository.findById(dish.getId()).orElseThrow();
    }

    protected void assertPersistedDishToMatchAllProperties(Dish expectedDish) {
        assertDishAllPropertiesEquals(expectedDish, getPersistedDish(expectedDish));
    }

    protected void assertPersistedDishToMatchUpdatableProperties(Dish expectedDish) {
        assertDishAllUpdatablePropertiesEquals(expectedDish, getPersistedDish(expectedDish));
    }
}
