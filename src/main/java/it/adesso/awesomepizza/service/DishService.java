package it.adesso.awesomepizza.service;

import it.adesso.awesomepizza.domain.Dish;
import it.adesso.awesomepizza.repository.DishRepository;
import it.adesso.awesomepizza.service.dto.DishDTO;
import it.adesso.awesomepizza.service.mapper.DishMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link it.adesso.awesomepizza.domain.Dish}.
 */
@Service
@Transactional
public class DishService {

    private static final Logger LOG = LoggerFactory.getLogger(DishService.class);

    private final DishRepository dishRepository;

    private final DishMapper dishMapper;

    public DishService(DishRepository dishRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.dishMapper = dishMapper;
    }

    /**
     * Save a dish.
     *
     * @param dishDTO the entity to save.
     * @return the persisted entity.
     */
    public DishDTO save(DishDTO dishDTO) {
        LOG.debug("Request to save Dish : {}", dishDTO);
        Dish dish = dishMapper.toEntity(dishDTO);
        dish = dishRepository.save(dish);
        return dishMapper.toDto(dish);
    }

    /**
     * Update a dish.
     *
     * @param dishDTO the entity to save.
     * @return the persisted entity.
     */
    public DishDTO update(DishDTO dishDTO) {
        LOG.debug("Request to update Dish : {}", dishDTO);
        Dish dish = dishMapper.toEntity(dishDTO);
        dish = dishRepository.save(dish);
        return dishMapper.toDto(dish);
    }

    /**
     * Partially update a dish.
     *
     * @param dishDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DishDTO> partialUpdate(DishDTO dishDTO) {
        LOG.debug("Request to partially update Dish : {}", dishDTO);

        return dishRepository
            .findById(dishDTO.getId())
            .map(existingDish -> {
                dishMapper.partialUpdate(existingDish, dishDTO);

                return existingDish;
            })
            .map(dishRepository::save)
            .map(dishMapper::toDto);
    }

    /**
     * Get all the dishes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DishDTO> findAll() {
        LOG.debug("Request to get all Dishes");
        return dishRepository.findAll().stream().map(dishMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one dish by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DishDTO> findOne(Long id) {
        LOG.debug("Request to get Dish : {}", id);
        return dishRepository.findById(id).map(dishMapper::toDto);
    }

    /**
     * Delete the dish by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Dish : {}", id);
        dishRepository.deleteById(id);
    }
}
