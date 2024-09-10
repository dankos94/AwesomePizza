package it.adesso.awesomepizza.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.adesso.awesomepizza.domain.PaymentStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String statusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentStatusDTO)) {
            return false;
        }

        PaymentStatusDTO paymentStatusDTO = (PaymentStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentStatusDTO{" +
            "id=" + getId() +
            ", statusName='" + getStatusName() + "'" +
            "}";
    }
}
