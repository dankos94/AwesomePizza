package it.adesso.awesomepizza.web.rest;

import static it.adesso.awesomepizza.domain.PizzaOrderAsserts.*;
import static it.adesso.awesomepizza.web.rest.TestUtil.createUpdateProxyForBean;
import static it.adesso.awesomepizza.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.adesso.awesomepizza.IntegrationTest;
import it.adesso.awesomepizza.domain.PizzaOrder;
import it.adesso.awesomepizza.repository.PizzaOrderRepository;
import it.adesso.awesomepizza.repository.UserRepository;
import it.adesso.awesomepizza.service.PizzaOrderService;
import it.adesso.awesomepizza.service.dto.PizzaOrderDTO;
import it.adesso.awesomepizza.service.mapper.PizzaOrderMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PizzaOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PizzaOrderResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/pizza-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PizzaOrderRepository pizzaOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PizzaOrderRepository pizzaOrderRepositoryMock;

    @Autowired
    private PizzaOrderMapper pizzaOrderMapper;

    @Mock
    private PizzaOrderService pizzaOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPizzaOrderMockMvc;

    private PizzaOrder pizzaOrder;

    private PizzaOrder insertedPizzaOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PizzaOrder createEntity() {
        return new PizzaOrder().orderDate(DEFAULT_ORDER_DATE).totalPrice(DEFAULT_TOTAL_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PizzaOrder createUpdatedEntity() {
        return new PizzaOrder().orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE);
    }

    @BeforeEach
    public void initTest() {
        pizzaOrder = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPizzaOrder != null) {
            pizzaOrderRepository.delete(insertedPizzaOrder);
            insertedPizzaOrder = null;
        }
    }

    @Test
    @Transactional
    void createPizzaOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PizzaOrder
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);
        var returnedPizzaOrderDTO = om.readValue(
            restPizzaOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PizzaOrderDTO.class
        );

        // Validate the PizzaOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPizzaOrder = pizzaOrderMapper.toEntity(returnedPizzaOrderDTO);
        assertPizzaOrderUpdatableFieldsEquals(returnedPizzaOrder, getPersistedPizzaOrder(returnedPizzaOrder));

        insertedPizzaOrder = returnedPizzaOrder;
    }

    @Test
    @Transactional
    void createPizzaOrderWithExistingId() throws Exception {
        // Create the PizzaOrder with an existing ID
        pizzaOrder.setId(1L);
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPizzaOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pizzaOrder.setOrderDate(null);

        // Create the PizzaOrder, which fails.
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        restPizzaOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pizzaOrder.setTotalPrice(null);

        // Create the PizzaOrder, which fails.
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        restPizzaOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPizzaOrders() throws Exception {
        // Initialize the database
        insertedPizzaOrder = pizzaOrderRepository.saveAndFlush(pizzaOrder);

        // Get all the pizzaOrderList
        restPizzaOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pizzaOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPizzaOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(pizzaOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPizzaOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pizzaOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPizzaOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pizzaOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPizzaOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pizzaOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPizzaOrder() throws Exception {
        // Initialize the database
        insertedPizzaOrder = pizzaOrderRepository.saveAndFlush(pizzaOrder);

        // Get the pizzaOrder
        restPizzaOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, pizzaOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pizzaOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingPizzaOrder() throws Exception {
        // Get the pizzaOrder
        restPizzaOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPizzaOrder() throws Exception {
        // Initialize the database
        insertedPizzaOrder = pizzaOrderRepository.saveAndFlush(pizzaOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pizzaOrder
        PizzaOrder updatedPizzaOrder = pizzaOrderRepository.findById(pizzaOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPizzaOrder are not directly saved in db
        em.detach(updatedPizzaOrder);
        updatedPizzaOrder.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE);
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(updatedPizzaOrder);

        restPizzaOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pizzaOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pizzaOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPizzaOrderToMatchAllProperties(updatedPizzaOrder);
    }

    @Test
    @Transactional
    void putNonExistingPizzaOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaOrder.setId(longCount.incrementAndGet());

        // Create the PizzaOrder
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPizzaOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pizzaOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pizzaOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPizzaOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaOrder.setId(longCount.incrementAndGet());

        // Create the PizzaOrder
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pizzaOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPizzaOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaOrder.setId(longCount.incrementAndGet());

        // Create the PizzaOrder
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pizzaOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePizzaOrderWithPatch() throws Exception {
        // Initialize the database
        insertedPizzaOrder = pizzaOrderRepository.saveAndFlush(pizzaOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pizzaOrder using partial update
        PizzaOrder partialUpdatedPizzaOrder = new PizzaOrder();
        partialUpdatedPizzaOrder.setId(pizzaOrder.getId());

        partialUpdatedPizzaOrder.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE);

        restPizzaOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPizzaOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPizzaOrder))
            )
            .andExpect(status().isOk());

        // Validate the PizzaOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPizzaOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPizzaOrder, pizzaOrder),
            getPersistedPizzaOrder(pizzaOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdatePizzaOrderWithPatch() throws Exception {
        // Initialize the database
        insertedPizzaOrder = pizzaOrderRepository.saveAndFlush(pizzaOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pizzaOrder using partial update
        PizzaOrder partialUpdatedPizzaOrder = new PizzaOrder();
        partialUpdatedPizzaOrder.setId(pizzaOrder.getId());

        partialUpdatedPizzaOrder.orderDate(UPDATED_ORDER_DATE).totalPrice(UPDATED_TOTAL_PRICE);

        restPizzaOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPizzaOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPizzaOrder))
            )
            .andExpect(status().isOk());

        // Validate the PizzaOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPizzaOrderUpdatableFieldsEquals(partialUpdatedPizzaOrder, getPersistedPizzaOrder(partialUpdatedPizzaOrder));
    }

    @Test
    @Transactional
    void patchNonExistingPizzaOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaOrder.setId(longCount.incrementAndGet());

        // Create the PizzaOrder
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPizzaOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pizzaOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pizzaOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPizzaOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaOrder.setId(longCount.incrementAndGet());

        // Create the PizzaOrder
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pizzaOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPizzaOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pizzaOrder.setId(longCount.incrementAndGet());

        // Create the PizzaOrder
        PizzaOrderDTO pizzaOrderDTO = pizzaOrderMapper.toDto(pizzaOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPizzaOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pizzaOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PizzaOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePizzaOrder() throws Exception {
        // Initialize the database
        insertedPizzaOrder = pizzaOrderRepository.saveAndFlush(pizzaOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pizzaOrder
        restPizzaOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, pizzaOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pizzaOrderRepository.count();
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

    protected PizzaOrder getPersistedPizzaOrder(PizzaOrder pizzaOrder) {
        return pizzaOrderRepository.findById(pizzaOrder.getId()).orElseThrow();
    }

    protected void assertPersistedPizzaOrderToMatchAllProperties(PizzaOrder expectedPizzaOrder) {
        assertPizzaOrderAllPropertiesEquals(expectedPizzaOrder, getPersistedPizzaOrder(expectedPizzaOrder));
    }

    protected void assertPersistedPizzaOrderToMatchUpdatableProperties(PizzaOrder expectedPizzaOrder) {
        assertPizzaOrderAllUpdatablePropertiesEquals(expectedPizzaOrder, getPersistedPizzaOrder(expectedPizzaOrder));
    }
}
