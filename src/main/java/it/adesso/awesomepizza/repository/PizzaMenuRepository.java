package it.adesso.awesomepizza.repository;

import it.adesso.awesomepizza.domain.PizzaMenu;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PizzaMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PizzaMenuRepository extends JpaRepository<PizzaMenu, Long> {
    @Query("select pizzaMenu from PizzaMenu pizzaMenu where pizzaMenu.user.login = ?#{authentication.name}")
    List<PizzaMenu> findByUserIsCurrentUser();
}
