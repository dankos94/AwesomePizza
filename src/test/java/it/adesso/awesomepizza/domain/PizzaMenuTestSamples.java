package it.adesso.awesomepizza.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PizzaMenuTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PizzaMenu getPizzaMenuSample1() {
        return new PizzaMenu().id(1L).name("name1").description("description1");
    }

    public static PizzaMenu getPizzaMenuSample2() {
        return new PizzaMenu().id(2L).name("name2").description("description2");
    }

    public static PizzaMenu getPizzaMenuRandomSampleGenerator() {
        return new PizzaMenu().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
