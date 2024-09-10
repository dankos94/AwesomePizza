package it.adesso.awesomepizza.repository;

import it.adesso.awesomepizza.domain.PaymentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {}
