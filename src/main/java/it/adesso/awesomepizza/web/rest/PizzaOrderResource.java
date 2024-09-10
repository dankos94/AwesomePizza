package it.adesso.awesomepizza.web.rest;

import it.adesso.awesomepizza.domain.PizzaOrder;
import it.adesso.awesomepizza.domain.PizzaOrder;
import it.adesso.awesomepizza.repository.PizzaOrderRepository;
import it.adesso.awesomepizza.repository.PizzaOrderRepository;
import it.adesso.awesomepizza.service.PizzaOrderService;
import it.adesso.awesomepizza.service.dto.PizzaOrderDTO;
import it.adesso.awesomepizza.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.adesso.awesomepizza.domain.PizzaOrder}.
 */
@RestController
@RequestMapping("/api/pizza-orders")
public class PizzaOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(PizzaOrderResource.class);

    private static final String ENTITY_NAME = "pizzaOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PizzaOrderService pizzaOrderService;

    private final PizzaOrderRepository pizzaOrderRepository;

    public PizzaOrderResource(PizzaOrderService pizzaOrderService, PizzaOrderRepository pizzaOrderRepository) {
        this.pizzaOrderService = pizzaOrderService;
        this.pizzaOrderRepository = pizzaOrderRepository;
    }

    private final Sinks.Many<PizzaOrderDTO> orderSink = Sinks.many().multicast().onBackpressureBuffer();

    @GetMapping(value = "/orders/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PizzaOrderDTO> streamOrders() {
        LOG.debug("REST request to stream orders via SSE");
        return orderSink.asFlux().share();
    }

    /**
     * {@code POST  /pizza-orders} : Create a new pizzaOrder.
     *
     * @param pizzaOrderDTO the pizzaOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pizzaOrderDTO, or with status {@code 400 (Bad Request)} if the pizzaOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PizzaOrderDTO> createPizzaOrder(@Valid @RequestBody PizzaOrderDTO pizzaOrderDTO) throws URISyntaxException {
        LOG.debug("REST request to save PizzaOrder : {}", pizzaOrderDTO);
        if (pizzaOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new pizzaOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pizzaOrderDTO = pizzaOrderService.save(pizzaOrderDTO);
        return ResponseEntity.created(new URI("/api/pizza-orders/" + pizzaOrderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pizzaOrderDTO.getId().toString()))
            .body(pizzaOrderDTO);
    }

    /**
     * {@code PUT  /pizza-orders/:id} : Updates an existing pizzaOrder.
     *
     * @param id the id of the pizzaOrderDTO to save.
     * @param pizzaOrderDTO the pizzaOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pizzaOrderDTO,
     * or with status {@code 400 (Bad Request)} if the pizzaOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pizzaOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PizzaOrderDTO> updatePizzaOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PizzaOrderDTO pizzaOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PizzaOrder : {}, {}", id, pizzaOrderDTO);
        if (pizzaOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pizzaOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pizzaOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pizzaOrderDTO = pizzaOrderService.update(pizzaOrderDTO);
        orderSink.tryEmitNext(pizzaOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pizzaOrderDTO.getId().toString()))
            .body(pizzaOrderDTO);
    }

    /**
     * {@code PATCH  /pizza-orders/:id} : Partial updates given fields of an existing pizzaOrder, field will ignore if it is null
     *
     * @param id the id of the pizzaOrderDTO to save.
     * @param pizzaOrderDTO the pizzaOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pizzaOrderDTO,
     * or with status {@code 400 (Bad Request)} if the pizzaOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pizzaOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pizzaOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PizzaOrderDTO> partialUpdatePizzaOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PizzaOrderDTO pizzaOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PizzaOrder partially : {}, {}", id, pizzaOrderDTO);
        if (pizzaOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pizzaOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pizzaOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PizzaOrderDTO> result = pizzaOrderService.partialUpdate(pizzaOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pizzaOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pizza-orders} : get all the pizzaOrders.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pizzaOrders in body.
     */
    @GetMapping("")
    public List<PizzaOrderDTO> getAllPizzaOrders(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PizzaOrders");
        return pizzaOrderService.findAll();
    }

    /**
     * {@code GET  /pizza-orders/:id} : get the "id" pizzaOrder.
     *
     * @param id the id of the pizzaOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pizzaOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PizzaOrderDTO> getPizzaOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PizzaOrder : {}", id);
        Optional<PizzaOrderDTO> pizzaOrderDTO = pizzaOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pizzaOrderDTO);
    }

    /**
     * {@code DELETE  /pizza-orders/:id} : delete the "id" pizzaOrder.
     *
     * @param id the id of the pizzaOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizzaOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PizzaOrder : {}", id);
        pizzaOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
