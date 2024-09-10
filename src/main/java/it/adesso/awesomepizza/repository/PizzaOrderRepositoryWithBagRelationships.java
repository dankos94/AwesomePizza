package it.adesso.awesomepizza.repository;

import it.adesso.awesomepizza.domain.PizzaOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PizzaOrderRepositoryWithBagRelationships {
    Optional<PizzaOrder> fetchBagRelationships(Optional<PizzaOrder> pizzaOrder);

    List<PizzaOrder> fetchBagRelationships(List<PizzaOrder> pizzaOrders);

    Page<PizzaOrder> fetchBagRelationships(Page<PizzaOrder> pizzaOrders);
}
