package it.adesso.awesomepizza.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderStatus getOrderStatusSample1() {
        return new OrderStatus().id(1L).statusName("statusName1");
    }

    public static OrderStatus getOrderStatusSample2() {
        return new OrderStatus().id(2L).statusName("statusName2");
    }

    public static OrderStatus getOrderStatusRandomSampleGenerator() {
        return new OrderStatus().id(longCount.incrementAndGet()).statusName(UUID.randomUUID().toString());
    }
}
