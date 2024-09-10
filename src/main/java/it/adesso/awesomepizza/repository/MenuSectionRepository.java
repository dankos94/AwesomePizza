package it.adesso.awesomepizza.repository;

import it.adesso.awesomepizza.domain.MenuSection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, Long> {}
