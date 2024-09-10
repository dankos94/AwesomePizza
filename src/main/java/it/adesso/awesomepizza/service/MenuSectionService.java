package it.adesso.awesomepizza.service;

import it.adesso.awesomepizza.domain.MenuSection;
import it.adesso.awesomepizza.repository.MenuSectionRepository;
import it.adesso.awesomepizza.service.dto.MenuSectionDTO;
import it.adesso.awesomepizza.service.mapper.MenuSectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link it.adesso.awesomepizza.domain.MenuSection}.
 */
@Service
@Transactional
public class MenuSectionService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuSectionService.class);

    private final MenuSectionRepository menuSectionRepository;

    private final MenuSectionMapper menuSectionMapper;

    public MenuSectionService(MenuSectionRepository menuSectionRepository, MenuSectionMapper menuSectionMapper) {
        this.menuSectionRepository = menuSectionRepository;
        this.menuSectionMapper = menuSectionMapper;
    }

    /**
     * Save a menuSection.
     *
     * @param menuSectionDTO the entity to save.
     * @return the persisted entity.
     */
    public MenuSectionDTO save(MenuSectionDTO menuSectionDTO) {
        LOG.debug("Request to save MenuSection : {}", menuSectionDTO);
        MenuSection menuSection = menuSectionMapper.toEntity(menuSectionDTO);
        menuSection = menuSectionRepository.save(menuSection);
        return menuSectionMapper.toDto(menuSection);
    }

    /**
     * Update a menuSection.
     *
     * @param menuSectionDTO the entity to save.
     * @return the persisted entity.
     */
    public MenuSectionDTO update(MenuSectionDTO menuSectionDTO) {
        LOG.debug("Request to update MenuSection : {}", menuSectionDTO);
        MenuSection menuSection = menuSectionMapper.toEntity(menuSectionDTO);
        menuSection = menuSectionRepository.save(menuSection);
        return menuSectionMapper.toDto(menuSection);
    }

    /**
     * Partially update a menuSection.
     *
     * @param menuSectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MenuSectionDTO> partialUpdate(MenuSectionDTO menuSectionDTO) {
        LOG.debug("Request to partially update MenuSection : {}", menuSectionDTO);

        return menuSectionRepository
            .findById(menuSectionDTO.getId())
            .map(existingMenuSection -> {
                menuSectionMapper.partialUpdate(existingMenuSection, menuSectionDTO);

                return existingMenuSection;
            })
            .map(menuSectionRepository::save)
            .map(menuSectionMapper::toDto);
    }

    /**
     * Get all the menuSections.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MenuSectionDTO> findAll() {
        LOG.debug("Request to get all MenuSections");
        return menuSectionRepository.findAll().stream().map(menuSectionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one menuSection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MenuSectionDTO> findOne(Long id) {
        LOG.debug("Request to get MenuSection : {}", id);
        return menuSectionRepository.findById(id).map(menuSectionMapper::toDto);
    }

    /**
     * Delete the menuSection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MenuSection : {}", id);
        menuSectionRepository.deleteById(id);
    }
}
