package it.adesso.awesomepizza.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link it.adesso.awesomepizza.domain.PizzaOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PizzaOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    @NotNull
    private BigDecimal totalPrice;

    private PaymentDTO payment;

    private UserDTO user;

    private OrderStatusDTO orderStatus;

    private Set<DishDTO> dishes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public OrderStatusDTO getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusDTO orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<DishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(Set<DishDTO> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PizzaOrderDTO)) {
            return false;
        }

        PizzaOrderDTO pizzaOrderDTO = (PizzaOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pizzaOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PizzaOrderDTO{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", payment=" + getPayment() +
            ", user=" + getUser() +
            ", orderStatus=" + getOrderStatus() +
            ", dishes=" + getDishes() +
            "}";
    }
}
