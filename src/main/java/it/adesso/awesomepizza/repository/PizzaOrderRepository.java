package it.adesso.awesomepizza.repository;

import it.adesso.awesomepizza.domain.PizzaOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PizzaOrder entity.
 *
 * When extending this class, extend PizzaOrderRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PizzaOrderRepository extends PizzaOrderRepositoryWithBagRelationships, JpaRepository<PizzaOrder, Long> {
    @Query("select pizzaOrder from PizzaOrder pizzaOrder where pizzaOrder.user.login = ?#{authentication.name}")
    List<PizzaOrder> findByUserIsCurrentUser();

    default Optional<PizzaOrder> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<PizzaOrder> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<PizzaOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
