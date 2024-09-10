package it.adesso.awesomepizza.repository;

import it.adesso.awesomepizza.domain.PizzaOrder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PizzaOrderRepositoryWithBagRelationshipsImpl implements PizzaOrderRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PIZZAORDERS_PARAMETER = "pizzaOrders";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PizzaOrder> fetchBagRelationships(Optional<PizzaOrder> pizzaOrder) {
        return pizzaOrder.map(this::fetchDishes);
    }

    @Override
    public Page<PizzaOrder> fetchBagRelationships(Page<PizzaOrder> pizzaOrders) {
        return new PageImpl<>(fetchBagRelationships(pizzaOrders.getContent()), pizzaOrders.getPageable(), pizzaOrders.getTotalElements());
    }

    @Override
    public List<PizzaOrder> fetchBagRelationships(List<PizzaOrder> pizzaOrders) {
        return Optional.of(pizzaOrders).map(this::fetchDishes).orElse(Collections.emptyList());
    }

    PizzaOrder fetchDishes(PizzaOrder result) {
        return entityManager
            .createQuery(
                "select pizzaOrder from PizzaOrder pizzaOrder left join fetch pizzaOrder.dishes where pizzaOrder.id = :id",
                PizzaOrder.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<PizzaOrder> fetchDishes(List<PizzaOrder> pizzaOrders) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, pizzaOrders.size()).forEach(index -> order.put(pizzaOrders.get(index).getId(), index));
        List<PizzaOrder> result = entityManager
            .createQuery(
                "select pizzaOrder from PizzaOrder pizzaOrder left join fetch pizzaOrder.dishes where pizzaOrder in :pizzaOrders",
                PizzaOrder.class
            )
            .setParameter(PIZZAORDERS_PARAMETER, pizzaOrders)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
