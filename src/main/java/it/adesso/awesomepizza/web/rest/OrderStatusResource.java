package it.adesso.awesomepizza.web.rest;

import it.adesso.awesomepizza.repository.OrderStatusRepository;
import it.adesso.awesomepizza.service.OrderStatusService;
import it.adesso.awesomepizza.service.dto.OrderStatusDTO;
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
 * REST controller for managing {@link it.adesso.awesomepizza.domain.OrderStatus}.
 */
@RestController
@RequestMapping("/api/order-statuses")
public class OrderStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderStatusResource.class);

    private static final String ENTITY_NAME = "orderStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderStatusService orderStatusService;

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusResource(OrderStatusService orderStatusService, OrderStatusRepository orderStatusRepository) {
        this.orderStatusService = orderStatusService;
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * {@code POST  /order-statuses} : Create a new orderStatus.
     *
     * @param orderStatusDTO the orderStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderStatusDTO, or with status {@code 400 (Bad Request)} if the orderStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderStatusDTO> createOrderStatus(@Valid @RequestBody OrderStatusDTO orderStatusDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderStatus : {}", orderStatusDTO);
        if (orderStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderStatusDTO = orderStatusService.save(orderStatusDTO);
        return ResponseEntity.created(new URI("/api/order-statuses/" + orderStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderStatusDTO.getId().toString()))
            .body(orderStatusDTO);
    }

    /**
     * {@code PUT  /order-statuses/:id} : Updates an existing orderStatus.
     *
     * @param id the id of the orderStatusDTO to save.
     * @param orderStatusDTO the orderStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderStatusDTO,
     * or with status {@code 400 (Bad Request)} if the orderStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderStatusDTO> updateOrderStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderStatusDTO orderStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderStatus : {}, {}", id, orderStatusDTO);
        if (orderStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderStatusDTO = orderStatusService.update(orderStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderStatusDTO.getId().toString()))
            .body(orderStatusDTO);
    }

    /**
     * {@code PATCH  /order-statuses/:id} : Partial updates given fields of an existing orderStatus, field will ignore if it is null
     *
     * @param id the id of the orderStatusDTO to save.
     * @param orderStatusDTO the orderStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderStatusDTO,
     * or with status {@code 400 (Bad Request)} if the orderStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderStatusDTO> partialUpdateOrderStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderStatusDTO orderStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderStatus partially : {}, {}", id, orderStatusDTO);
        if (orderStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderStatusDTO> result = orderStatusService.partialUpdate(orderStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-statuses} : get all the orderStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderStatuses in body.
     */
    @GetMapping("")
    public List<OrderStatusDTO> getAllOrderStatuses() {
        LOG.debug("REST request to get all OrderStatuses");
        return orderStatusService.findAll();
    }

    /**
     * {@code GET  /order-statuses/:id} : get the "id" orderStatus.
     *
     * @param id the id of the orderStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusDTO> getOrderStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderStatus : {}", id);
        Optional<OrderStatusDTO> orderStatusDTO = orderStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderStatusDTO);
    }

    /**
     * {@code DELETE  /order-statuses/:id} : delete the "id" orderStatus.
     *
     * @param id the id of the orderStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderStatus : {}", id);
        orderStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
