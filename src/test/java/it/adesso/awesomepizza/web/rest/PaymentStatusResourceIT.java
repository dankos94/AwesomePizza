package it.adesso.awesomepizza.web.rest;

import static it.adesso.awesomepizza.domain.PaymentStatusAsserts.*;
import static it.adesso.awesomepizza.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.adesso.awesomepizza.IntegrationTest;
import it.adesso.awesomepizza.domain.PaymentStatus;
import it.adesso.awesomepizza.repository.PaymentStatusRepository;
import it.adesso.awesomepizza.service.dto.PaymentStatusDTO;
import it.adesso.awesomepizza.service.mapper.PaymentStatusMapper;
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
 * Integration tests for the {@link PaymentStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentStatusResourceIT {

    private static final String DEFAULT_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private PaymentStatusMapper paymentStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentStatusMockMvc;

    private PaymentStatus paymentStatus;

    private PaymentStatus insertedPaymentStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentStatus createEntity() {
        return new PaymentStatus().statusName(DEFAULT_STATUS_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentStatus createUpdatedEntity() {
        return new PaymentStatus().statusName(UPDATED_STATUS_NAME);
    }

    @BeforeEach
    public void initTest() {
        paymentStatus = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentStatus != null) {
            paymentStatusRepository.delete(insertedPaymentStatus);
            insertedPaymentStatus = null;
        }
    }

    @Test
    @Transactional
    void createPaymentStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentStatus
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);
        var returnedPaymentStatusDTO = om.readValue(
            restPaymentStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentStatusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentStatusDTO.class
        );

        // Validate the PaymentStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaymentStatus = paymentStatusMapper.toEntity(returnedPaymentStatusDTO);
        assertPaymentStatusUpdatableFieldsEquals(returnedPaymentStatus, getPersistedPaymentStatus(returnedPaymentStatus));

        insertedPaymentStatus = returnedPaymentStatus;
    }

    @Test
    @Transactional
    void createPaymentStatusWithExistingId() throws Exception {
        // Create the PaymentStatus with an existing ID
        paymentStatus.setId(1L);
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentStatus.setStatusName(null);

        // Create the PaymentStatus, which fails.
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        restPaymentStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentStatuses() throws Exception {
        // Initialize the database
        insertedPaymentStatus = paymentStatusRepository.saveAndFlush(paymentStatus);

        // Get all the paymentStatusList
        restPaymentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)));
    }

    @Test
    @Transactional
    void getPaymentStatus() throws Exception {
        // Initialize the database
        insertedPaymentStatus = paymentStatusRepository.saveAndFlush(paymentStatus);

        // Get the paymentStatus
        restPaymentStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusName").value(DEFAULT_STATUS_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPaymentStatus() throws Exception {
        // Get the paymentStatus
        restPaymentStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentStatus() throws Exception {
        // Initialize the database
        insertedPaymentStatus = paymentStatusRepository.saveAndFlush(paymentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentStatus
        PaymentStatus updatedPaymentStatus = paymentStatusRepository.findById(paymentStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentStatus are not directly saved in db
        em.detach(updatedPaymentStatus);
        updatedPaymentStatus.statusName(UPDATED_STATUS_NAME);
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(updatedPaymentStatus);

        restPaymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentStatusToMatchAllProperties(updatedPaymentStatus);
    }

    @Test
    @Transactional
    void putNonExistingPaymentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentStatus.setId(longCount.incrementAndGet());

        // Create the PaymentStatus
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentStatus.setId(longCount.incrementAndGet());

        // Create the PaymentStatus
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentStatus.setId(longCount.incrementAndGet());

        // Create the PaymentStatus
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentStatusWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentStatus = paymentStatusRepository.saveAndFlush(paymentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentStatus using partial update
        PaymentStatus partialUpdatedPaymentStatus = new PaymentStatus();
        partialUpdatedPaymentStatus.setId(paymentStatus.getId());

        partialUpdatedPaymentStatus.statusName(UPDATED_STATUS_NAME);

        restPaymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentStatus))
            )
            .andExpect(status().isOk());

        // Validate the PaymentStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentStatus, paymentStatus),
            getPersistedPaymentStatus(paymentStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentStatusWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentStatus = paymentStatusRepository.saveAndFlush(paymentStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentStatus using partial update
        PaymentStatus partialUpdatedPaymentStatus = new PaymentStatus();
        partialUpdatedPaymentStatus.setId(paymentStatus.getId());

        partialUpdatedPaymentStatus.statusName(UPDATED_STATUS_NAME);

        restPaymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentStatus))
            )
            .andExpect(status().isOk());

        // Validate the PaymentStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentStatusUpdatableFieldsEquals(partialUpdatedPaymentStatus, getPersistedPaymentStatus(partialUpdatedPaymentStatus));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentStatus.setId(longCount.incrementAndGet());

        // Create the PaymentStatus
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentStatus.setId(longCount.incrementAndGet());

        // Create the PaymentStatus
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentStatus.setId(longCount.incrementAndGet());

        // Create the PaymentStatus
        PaymentStatusDTO paymentStatusDTO = paymentStatusMapper.toDto(paymentStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentStatus() throws Exception {
        // Initialize the database
        insertedPaymentStatus = paymentStatusRepository.saveAndFlush(paymentStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentStatus
        restPaymentStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentStatusRepository.count();
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

    protected PaymentStatus getPersistedPaymentStatus(PaymentStatus paymentStatus) {
        return paymentStatusRepository.findById(paymentStatus.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentStatusToMatchAllProperties(PaymentStatus expectedPaymentStatus) {
        assertPaymentStatusAllPropertiesEquals(expectedPaymentStatus, getPersistedPaymentStatus(expectedPaymentStatus));
    }

    protected void assertPersistedPaymentStatusToMatchUpdatableProperties(PaymentStatus expectedPaymentStatus) {
        assertPaymentStatusAllUpdatablePropertiesEquals(expectedPaymentStatus, getPersistedPaymentStatus(expectedPaymentStatus));
    }
}
