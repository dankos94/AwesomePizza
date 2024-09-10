package it.adesso.awesomepizza.web.rest;

import it.adesso.awesomepizza.repository.PaymentStatusRepository;
import it.adesso.awesomepizza.service.PaymentStatusService;
import it.adesso.awesomepizza.service.dto.PaymentStatusDTO;
import it.adesso.awesomepizza.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.adesso.awesomepizza.domain.PaymentStatus}.
 */
@RestController
@RequestMapping("/api/payment-statuses")
public class PaymentStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentStatusResource.class);

    private static final String ENTITY_NAME = "paymentStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentStatusService paymentStatusService;

    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentStatusResource(PaymentStatusService paymentStatusService, PaymentStatusRepository paymentStatusRepository) {
        this.paymentStatusService = paymentStatusService;
        this.paymentStatusRepository = paymentStatusRepository;
    }

    /**
     * {@code POST  /payment-statuses} : Create a new paymentStatus.
     *
     * @param paymentStatusDTO the paymentStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentStatusDTO, or with status {@code 400 (Bad Request)} if the paymentStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentStatusDTO> createPaymentStatus(@Valid @RequestBody PaymentStatusDTO paymentStatusDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentStatus : {}", paymentStatusDTO);
        if (paymentStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentStatusDTO = paymentStatusService.save(paymentStatusDTO);
        return ResponseEntity.created(new URI("/api/payment-statuses/" + paymentStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentStatusDTO.getId().toString()))
            .body(paymentStatusDTO);
    }

    /**
     * {@code PUT  /payment-statuses/:id} : Updates an existing paymentStatus.
     *
     * @param id the id of the paymentStatusDTO to save.
     * @param paymentStatusDTO the paymentStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentStatusDTO,
     * or with status {@code 400 (Bad Request)} if the paymentStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentStatusDTO> updatePaymentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentStatusDTO paymentStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentStatus : {}, {}", id, paymentStatusDTO);
        if (paymentStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentStatusDTO = paymentStatusService.update(paymentStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentStatusDTO.getId().toString()))
            .body(paymentStatusDTO);
    }

    /**
     * {@code PATCH  /payment-statuses/:id} : Partial updates given fields of an existing paymentStatus, field will ignore if it is null
     *
     * @param id the id of the paymentStatusDTO to save.
     * @param paymentStatusDTO the paymentStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentStatusDTO,
     * or with status {@code 400 (Bad Request)} if the paymentStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentStatusDTO> partialUpdatePaymentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentStatusDTO paymentStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentStatus partially : {}, {}", id, paymentStatusDTO);
        if (paymentStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentStatusDTO> result = paymentStatusService.partialUpdate(paymentStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-statuses} : get all the paymentStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentStatuses in body.
     */
    @GetMapping("")
    public List<PaymentStatusDTO> getAllPaymentStatuses() {
        LOG.debug("REST request to get all PaymentStatuses");
        return paymentStatusService.findAll();
    }

    /**
     * {@code GET  /payment-statuses/:id} : get the "id" paymentStatus.
     *
     * @param id the id of the paymentStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentStatusDTO> getPaymentStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentStatus : {}", id);
        Optional<PaymentStatusDTO> paymentStatusDTO = paymentStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentStatusDTO);
    }

    /**
     * {@code DELETE  /payment-statuses/:id} : delete the "id" paymentStatus.
     *
     * @param id the id of the paymentStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentStatus : {}", id);
        paymentStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
