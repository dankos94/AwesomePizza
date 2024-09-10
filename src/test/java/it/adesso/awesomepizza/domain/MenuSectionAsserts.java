package it.adesso.awesomepizza.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuSectionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMenuSectionAllPropertiesEquals(MenuSection expected, MenuSection actual) {
        assertMenuSectionAutoGeneratedPropertiesEquals(expected, actual);
        assertMenuSectionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMenuSectionAllUpdatablePropertiesEquals(MenuSection expected, MenuSection actual) {
        assertMenuSectionUpdatableFieldsEquals(expected, actual);
        assertMenuSectionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMenuSectionAutoGeneratedPropertiesEquals(MenuSection expected, MenuSection actual) {
        assertThat(expected)
            .as("Verify MenuSection auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMenuSectionUpdatableFieldsEquals(MenuSection expected, MenuSection actual) {
        assertThat(expected)
            .as("Verify MenuSection relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMenuSectionUpdatableRelationshipsEquals(MenuSection expected, MenuSection actual) {
        assertThat(expected)
            .as("Verify MenuSection relationships")
            .satisfies(e -> assertThat(e.getPizzaMenu()).as("check pizzaMenu").isEqualTo(actual.getPizzaMenu()));
    }
}
