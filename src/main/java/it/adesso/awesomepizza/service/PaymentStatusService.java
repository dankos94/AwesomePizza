package it.adesso.awesomepizza.service;

import it.adesso.awesomepizza.domain.PaymentStatus;
import it.adesso.awesomepizza.repository.PaymentStatusRepository;
import it.adesso.awesomepizza.service.dto.PaymentStatusDTO;
import it.adesso.awesomepizza.service.mapper.PaymentStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link it.adesso.awesomepizza.domain.PaymentStatus}.
 */
@Service
@Transactional
public class PaymentStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentStatusService.class);

    private final PaymentStatusRepository paymentStatusRepository;

    private final PaymentStatusMapper paymentStatusMapper;

    public PaymentStatusService(PaymentStatusRepository paymentStatusRepository, PaymentStatusMapper paymentStatusMapper) {
        this.paymentStatusRepository = paymentStatusRepository;
        this.paymentStatusMapper = paymentStatusMapper;
    }

    /**
     * Save a paymentStatus.
     *
     * @param paymentStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentStatusDTO save(PaymentStatusDTO paymentStatusDTO) {
        LOG.debug("Request to save PaymentStatus : {}", paymentStatusDTO);
        PaymentStatus paymentStatus = paymentStatusMapper.toEntity(paymentStatusDTO);
        paymentStatus = paymentStatusRepository.save(paymentStatus);
        return paymentStatusMapper.toDto(paymentStatus);
    }

    /**
     * Update a paymentStatus.
     *
     * @param paymentStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentStatusDTO update(PaymentStatusDTO paymentStatusDTO) {
        LOG.debug("Request to update PaymentStatus : {}", paymentStatusDTO);
        PaymentStatus paymentStatus = paymentStatusMapper.toEntity(paymentStatusDTO);
        paymentStatus = paymentStatusRepository.save(paymentStatus);
        return paymentStatusMapper.toDto(paymentStatus);
    }

    /**
     * Partially update a paymentStatus.
     *
     * @param paymentStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentStatusDTO> partialUpdate(PaymentStatusDTO paymentStatusDTO) {
        LOG.debug("Request to partially update PaymentStatus : {}", paymentStatusDTO);

        return paymentStatusRepository
            .findById(paymentStatusDTO.getId())
            .map(existingPaymentStatus -> {
                paymentStatusMapper.partialUpdate(existingPaymentStatus, paymentStatusDTO);

                return existingPaymentStatus;
            })
            .map(paymentStatusRepository::save)
            .map(paymentStatusMapper::toDto);
    }

    /**
     * Get all the paymentStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentStatusDTO> findAll() {
        LOG.debug("Request to get all PaymentStatuses");
        return paymentStatusRepository.findAll().stream().map(paymentStatusMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one paymentStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentStatusDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentStatus : {}", id);
        return paymentStatusRepository.findById(id).map(paymentStatusMapper::toDto);
    }

    /**
     * Delete the paymentStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentStatus : {}", id);
        paymentStatusRepository.deleteById(id);
    }
}
