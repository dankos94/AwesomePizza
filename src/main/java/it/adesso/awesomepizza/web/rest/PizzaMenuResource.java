package it.adesso.awesomepizza.web.rest;

import it.adesso.awesomepizza.repository.PizzaMenuRepository;
import it.adesso.awesomepizza.service.PizzaMenuService;
import it.adesso.awesomepizza.service.dto.PizzaMenuDTO;
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
 * REST controller for managing {@link it.adesso.awesomepizza.domain.PizzaMenu}.
 */
@RestController
@RequestMapping("/api/pizza-menus")
public class PizzaMenuResource {

    private static final Logger LOG = LoggerFactory.getLogger(PizzaMenuResource.class);

    private static final String ENTITY_NAME = "pizzaMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PizzaMenuService pizzaMenuService;

    private final PizzaMenuRepository pizzaMenuRepository;

    public PizzaMenuResource(PizzaMenuService pizzaMenuService, PizzaMenuRepository pizzaMenuRepository) {
        this.pizzaMenuService = pizzaMenuService;
        this.pizzaMenuRepository = pizzaMenuRepository;
    }

    /**
     * {@code POST  /pizza-menus} : Create a new pizzaMenu.
     *
     * @param pizzaMenuDTO the pizzaMenuDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pizzaMenuDTO, or with status {@code 400 (Bad Request)} if the pizzaMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PizzaMenuDTO> createPizzaMenu(@Valid @RequestBody PizzaMenuDTO pizzaMenuDTO) throws URISyntaxException {
        LOG.debug("REST request to save PizzaMenu : {}", pizzaMenuDTO);
        if (pizzaMenuDTO.getId() != null) {
            throw new BadRequestAlertException("A new pizzaMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pizzaMenuDTO = pizzaMenuService.save(pizzaMenuDTO);
        return ResponseEntity.created(new URI("/api/pizza-menus/" + pizzaMenuDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pizzaMenuDTO.getId().toString()))
            .body(pizzaMenuDTO);
    }

    /**
     * {@code PUT  /pizza-menus/:id} : Updates an existing pizzaMenu.
     *
     * @param id the id of the pizzaMenuDTO to save.
     * @param pizzaMenuDTO the pizzaMenuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pizzaMenuDTO,
     * or with status {@code 400 (Bad Request)} if the pizzaMenuDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pizzaMenuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PizzaMenuDTO> updatePizzaMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PizzaMenuDTO pizzaMenuDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PizzaMenu : {}, {}", id, pizzaMenuDTO);
        if (pizzaMenuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pizzaMenuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pizzaMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pizzaMenuDTO = pizzaMenuService.update(pizzaMenuDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pizzaMenuDTO.getId().toString()))
            .body(pizzaMenuDTO);
    }

    /**
     * {@code PATCH  /pizza-menus/:id} : Partial updates given fields of an existing pizzaMenu, field will ignore if it is null
     *
     * @param id the id of the pizzaMenuDTO to save.
     * @param pizzaMenuDTO the pizzaMenuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pizzaMenuDTO,
     * or with status {@code 400 (Bad Request)} if the pizzaMenuDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pizzaMenuDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pizzaMenuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PizzaMenuDTO> partialUpdatePizzaMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PizzaMenuDTO pizzaMenuDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PizzaMenu partially : {}, {}", id, pizzaMenuDTO);
        if (pizzaMenuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pizzaMenuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pizzaMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PizzaMenuDTO> result = pizzaMenuService.partialUpdate(pizzaMenuDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pizzaMenuDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pizza-menus} : get all the pizzaMenus.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pizzaMenus in body.
     */
    @GetMapping("")
    public List<PizzaMenuDTO> getAllPizzaMenus() {
        LOG.debug("REST request to get all PizzaMenus");
        return pizzaMenuService.findAll();
    }

    /**
     * {@code GET  /pizza-menus/:id} : get the "id" pizzaMenu.
     *
     * @param id the id of the pizzaMenuDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pizzaMenuDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PizzaMenuDTO> getPizzaMenu(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PizzaMenu : {}", id);
        Optional<PizzaMenuDTO> pizzaMenuDTO = pizzaMenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pizzaMenuDTO);
    }

    /**
     * {@code DELETE  /pizza-menus/:id} : delete the "id" pizzaMenu.
     *
     * @param id the id of the pizzaMenuDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizzaMenu(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PizzaMenu : {}", id);
        pizzaMenuService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
