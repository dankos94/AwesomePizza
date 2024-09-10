package it.adesso.awesomepizza.service;

import it.adesso.awesomepizza.domain.PizzaOrder;
import it.adesso.awesomepizza.repository.PizzaOrderRepository;
import it.adesso.awesomepizza.service.dto.PizzaOrderDTO;
import it.adesso.awesomepizza.service.mapper.PizzaOrderMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link it.adesso.awesomepizza.domain.PizzaOrder}.
 */
@Service
@Transactional
public class PizzaOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(PizzaOrderService.class);

    private final PizzaOrderRepository pizzaOrderRepository;

    private final PizzaOrderMapper pizzaOrderMapper;

    public PizzaOrderService(PizzaOrderRepository pizzaOrderRepository, PizzaOrderMapper pizzaOrderMapper) {
        this.pizzaOrderRepository = pizzaOrderRepository;
        this.pizzaOrderMapper = pizzaOrderMapper;
    }

    /**
     * Save a pizzaOrder.
     *
     * @param pizzaOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public PizzaOrderDTO save(PizzaOrderDTO pizzaOrderDTO) {
        LOG.debug("Request to save PizzaOrder : {}", pizzaOrderDTO);
        PizzaOrder pizzaOrder = pizzaOrderMapper.toEntity(pizzaOrderDTO);
        pizzaOrder = pizzaOrderRepository.save(pizzaOrder);
        return pizzaOrderMapper.toDto(pizzaOrder);
    }

    /**
     * Update a pizzaOrder.
     *
     * @param pizzaOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public PizzaOrderDTO update(PizzaOrderDTO pizzaOrderDTO) {
        LOG.debug("Request to update PizzaOrder : {}", pizzaOrderDTO);
        PizzaOrder pizzaOrder = pizzaOrderMapper.toEntity(pizzaOrderDTO);
        pizzaOrder = pizzaOrderRepository.save(pizzaOrder);
        return pizzaOrderMapper.toDto(pizzaOrder);
    }

    /**
     * Partially update a pizzaOrder.
     *
     * @param pizzaOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PizzaOrderDTO> partialUpdate(PizzaOrderDTO pizzaOrderDTO) {
        LOG.debug("Request to partially update PizzaOrder : {}", pizzaOrderDTO);

        return pizzaOrderRepository
            .findById(pizzaOrderDTO.getId())
            .map(existingPizzaOrder -> {
                pizzaOrderMapper.partialUpdate(existingPizzaOrder, pizzaOrderDTO);

                return existingPizzaOrder;
            })
            .map(pizzaOrderRepository::save)
            .map(pizzaOrderMapper::toDto);
    }

    /**
     * Get all the pizzaOrders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PizzaOrderDTO> findAll() {
        LOG.debug("Request to get all PizzaOrders");
        return pizzaOrderRepository.findAll().stream().map(pizzaOrderMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the pizzaOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PizzaOrderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pizzaOrderRepository.findAllWithEagerRelationships(pageable).map(pizzaOrderMapper::toDto);
    }

    /**
     * Get one pizzaOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PizzaOrderDTO> findOne(Long id) {
        LOG.debug("Request to get PizzaOrder : {}", id);
        return pizzaOrderRepository.findOneWithEagerRelationships(id).map(pizzaOrderMapper::toDto);
    }

    /**
     * Delete the pizzaOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PizzaOrder : {}", id);
        pizzaOrderRepository.deleteById(id);
    }
}
