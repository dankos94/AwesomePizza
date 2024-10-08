package it.adesso.awesomepizza.web.rest;

import it.adesso.awesomepizza.repository.MenuSectionRepository;
import it.adesso.awesomepizza.service.MenuSectionService;
import it.adesso.awesomepizza.service.dto.MenuSectionDTO;
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
 * REST controller for managing {@link it.adesso.awesomepizza.domain.MenuSection}.
 */
@RestController
@RequestMapping("/api/menu-sections")
public class MenuSectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(MenuSectionResource.class);

    private static final String ENTITY_NAME = "menuSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuSectionService menuSectionService;

    private final MenuSectionRepository menuSectionRepository;

    public MenuSectionResource(MenuSectionService menuSectionService, MenuSectionRepository menuSectionRepository) {
        this.menuSectionService = menuSectionService;
        this.menuSectionRepository = menuSectionRepository;
    }

    /**
     * {@code POST  /menu-sections} : Create a new menuSection.
     *
     * @param menuSectionDTO the menuSectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuSectionDTO, or with status {@code 400 (Bad Request)} if the menuSection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MenuSectionDTO> createMenuSection(@Valid @RequestBody MenuSectionDTO menuSectionDTO) throws URISyntaxException {
        LOG.debug("REST request to save MenuSection : {}", menuSectionDTO);
        if (menuSectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuSectionDTO = menuSectionService.save(menuSectionDTO);
        return ResponseEntity.created(new URI("/api/menu-sections/" + menuSectionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, menuSectionDTO.getId().toString()))
            .body(menuSectionDTO);
    }

    /**
     * {@code PUT  /menu-sections/:id} : Updates an existing menuSection.
     *
     * @param id the id of the menuSectionDTO to save.
     * @param menuSectionDTO the menuSectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuSectionDTO,
     * or with status {@code 400 (Bad Request)} if the menuSectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuSectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuSectionDTO> updateMenuSection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MenuSectionDTO menuSectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MenuSection : {}, {}", id, menuSectionDTO);
        if (menuSectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuSectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        menuSectionDTO = menuSectionService.update(menuSectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuSectionDTO.getId().toString()))
            .body(menuSectionDTO);
    }

    /**
     * {@code PATCH  /menu-sections/:id} : Partial updates given fields of an existing menuSection, field will ignore if it is null
     *
     * @param id the id of the menuSectionDTO to save.
     * @param menuSectionDTO the menuSectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuSectionDTO,
     * or with status {@code 400 (Bad Request)} if the menuSectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the menuSectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuSectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuSectionDTO> partialUpdateMenuSection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MenuSectionDTO menuSectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MenuSection partially : {}, {}", id, menuSectionDTO);
        if (menuSectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuSectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuSectionDTO> result = menuSectionService.partialUpdate(menuSectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuSectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-sections} : get all the menuSections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuSections in body.
     */
    @GetMapping("")
    public List<MenuSectionDTO> getAllMenuSections() {
        LOG.debug("REST request to get all MenuSections");
        return menuSectionService.findAll();
    }

    /**
     * {@code GET  /menu-sections/:id} : get the "id" menuSection.
     *
     * @param id the id of the menuSectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuSectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuSectionDTO> getMenuSection(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MenuSection : {}", id);
        Optional<MenuSectionDTO> menuSectionDTO = menuSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuSectionDTO);
    }

    /**
     * {@code DELETE  /menu-sections/:id} : delete the "id" menuSection.
     *
     * @param id the id of the menuSectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuSection(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MenuSection : {}", id);
        menuSectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
