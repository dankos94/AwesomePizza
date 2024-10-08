package it.adesso.awesomepizza.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentStatusAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentStatusAllPropertiesEquals(PaymentStatus expected, PaymentStatus actual) {
        assertPaymentStatusAutoGeneratedPropertiesEquals(expected, actual);
        assertPaymentStatusAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentStatusAllUpdatablePropertiesEquals(PaymentStatus expected, PaymentStatus actual) {
        assertPaymentStatusUpdatableFieldsEquals(expected, actual);
        assertPaymentStatusUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentStatusAutoGeneratedPropertiesEquals(PaymentStatus expected, PaymentStatus actual) {
        assertThat(expected)
            .as("Verify PaymentStatus auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentStatusUpdatableFieldsEquals(PaymentStatus expected, PaymentStatus actual) {
        assertThat(expected)
            .as("Verify PaymentStatus relevant properties")
            .satisfies(e -> assertThat(e.getStatusName()).as("check statusName").isEqualTo(actual.getStatusName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentStatusUpdatableRelationshipsEquals(PaymentStatus expected, PaymentStatus actual) {
        // empty method
    }
}
