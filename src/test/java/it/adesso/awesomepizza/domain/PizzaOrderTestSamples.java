package it.adesso.awesomepizza.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PizzaOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PizzaOrder getPizzaOrderSample1() {
        return new PizzaOrder().id(1L);
    }

    public static PizzaOrder getPizzaOrderSample2() {
        return new PizzaOrder().id(2L);
    }

    public static PizzaOrder getPizzaOrderRandomSampleGenerator() {
        return new PizzaOrder().id(longCount.incrementAndGet());
    }
}
