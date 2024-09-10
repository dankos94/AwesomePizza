package it.adesso.awesomepizza.web.rest;

import it.adesso.awesomepizza.repository.DishRepository;
import it.adesso.awesomepizza.service.DishService;
import it.adesso.awesomepizza.service.dto.DishDTO;
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
 * REST controller for managing {@link it.adesso.awesomepizza.domain.Dish}.
 */
@RestController
@RequestMapping("/api/dishes")
public class DishResource {

    private static final Logger LOG = LoggerFactory.getLogger(DishResource.class);

    private static final String ENTITY_NAME = "dish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DishService dishService;

    private final DishRepository dishRepository;

    public DishResource(DishService dishService, DishRepository dishRepository) {
        this.dishService = dishService;
        this.dishRepository = dishRepository;
    }

    /**
     * {@code POST  /dishes} : Create a new dish.
     *
     * @param dishDTO the dishDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dishDTO, or with status {@code 400 (Bad Request)} if the dish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DishDTO> createDish(@Valid @RequestBody DishDTO dishDTO) throws URISyntaxException {
        LOG.debug("REST request to save Dish : {}", dishDTO);
        if (dishDTO.getId() != null) {
            throw new BadRequestAlertException("A new dish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dishDTO = dishService.save(dishDTO);
        return ResponseEntity.created(new URI("/api/dishes/" + dishDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dishDTO.getId().toString()))
            .body(dishDTO);
    }

    /**
     * {@code PUT  /dishes/:id} : Updates an existing dish.
     *
     * @param id the id of the dishDTO to save.
     * @param dishDTO the dishDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dishDTO,
     * or with status {@code 400 (Bad Request)} if the dishDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dishDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DishDTO> updateDish(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DishDTO dishDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Dish : {}, {}", id, dishDTO);
        if (dishDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dishDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dishDTO = dishService.update(dishDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dishDTO.getId().toString()))
            .body(dishDTO);
    }

    /**
     * {@code PATCH  /dishes/:id} : Partial updates given fields of an existing dish, field will ignore if it is null
     *
     * @param id the id of the dishDTO to save.
     * @param dishDTO the dishDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dishDTO,
     * or with status {@code 400 (Bad Request)} if the dishDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dishDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dishDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DishDTO> partialUpdateDish(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DishDTO dishDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Dish partially : {}, {}", id, dishDTO);
        if (dishDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dishDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DishDTO> result = dishService.partialUpdate(dishDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dishDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dishes} : get all the dishes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dishes in body.
     */
    @GetMapping("")
    public List<DishDTO> getAllDishes() {
        LOG.debug("REST request to get all Dishes");
        return dishService.findAll();
    }

    /**
     * {@code GET  /dishes/:id} : get the "id" dish.
     *
     * @param id the id of the dishDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dishDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDish(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Dish : {}", id);
        Optional<DishDTO> dishDTO = dishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dishDTO);
    }

    /**
     * {@code DELETE  /dishes/:id} : delete the "id" dish.
     *
     * @param id the id of the dishDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Dish : {}", id);
        dishService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
