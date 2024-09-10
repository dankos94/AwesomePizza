package it.adesso.awesomepizza.service;

import it.adesso.awesomepizza.domain.PizzaMenu;
import it.adesso.awesomepizza.repository.PizzaMenuRepository;
import it.adesso.awesomepizza.service.dto.PizzaMenuDTO;
import it.adesso.awesomepizza.service.mapper.PizzaMenuMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link it.adesso.awesomepizza.domain.PizzaMenu}.
 */
@Service
@Transactional
public class PizzaMenuService {

    private static final Logger LOG = LoggerFactory.getLogger(PizzaMenuService.class);

    private final PizzaMenuRepository pizzaMenuRepository;

    private final PizzaMenuMapper pizzaMenuMapper;

    public PizzaMenuService(PizzaMenuRepository pizzaMenuRepository, PizzaMenuMapper pizzaMenuMapper) {
        this.pizzaMenuRepository = pizzaMenuRepository;
        this.pizzaMenuMapper = pizzaMenuMapper;
    }

    /**
     * Save a pizzaMenu.
     *
     * @param pizzaMenuDTO the entity to save.
     * @return the persisted entity.
     */
    public PizzaMenuDTO save(PizzaMenuDTO pizzaMenuDTO) {
        LOG.debug("Request to save PizzaMenu : {}", pizzaMenuDTO);
        PizzaMenu pizzaMenu = pizzaMenuMapper.toEntity(pizzaMenuDTO);
        pizzaMenu = pizzaMenuRepository.save(pizzaMenu);
        return pizzaMenuMapper.toDto(pizzaMenu);
    }

    /**
     * Update a pizzaMenu.
     *
     * @param pizzaMenuDTO the entity to save.
     * @return the persisted entity.
     */
    public PizzaMenuDTO update(PizzaMenuDTO pizzaMenuDTO) {
        LOG.debug("Request to update PizzaMenu : {}", pizzaMenuDTO);
        PizzaMenu pizzaMenu = pizzaMenuMapper.toEntity(pizzaMenuDTO);
        pizzaMenu = pizzaMenuRepository.save(pizzaMenu);
        return pizzaMenuMapper.toDto(pizzaMenu);
    }

    /**
     * Partially update a pizzaMenu.
     *
     * @param pizzaMenuDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PizzaMenuDTO> partialUpdate(PizzaMenuDTO pizzaMenuDTO) {
        LOG.debug("Request to partially update PizzaMenu : {}", pizzaMenuDTO);

        return pizzaMenuRepository
            .findById(pizzaMenuDTO.getId())
            .map(existingPizzaMenu -> {
                pizzaMenuMapper.partialUpdate(existingPizzaMenu, pizzaMenuDTO);

                return existingPizzaMenu;
            })
            .map(pizzaMenuRepository::save)
            .map(pizzaMenuMapper::toDto);
    }

    /**
     * Get all the pizzaMenus.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PizzaMenuDTO> findAll() {
        LOG.debug("Request to get all PizzaMenus");
        return pizzaMenuRepository.findAll().stream().map(pizzaMenuMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pizzaMenu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PizzaMenuDTO> findOne(Long id) {
        LOG.debug("Request to get PizzaMenu : {}", id);
        return pizzaMenuRepository.findById(id).map(pizzaMenuMapper::toDto);
    }

    /**
     * Delete the pizzaMenu by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PizzaMenu : {}", id);
        pizzaMenuRepository.deleteById(id);
    }
}
