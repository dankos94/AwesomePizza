package it.adesso.awesomepizza.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentStatus getPaymentStatusSample1() {
        return new PaymentStatus().id(1L).statusName("statusName1");
    }

    public static PaymentStatus getPaymentStatusSample2() {
        return new PaymentStatus().id(2L).statusName("statusName2");
    }

    public static PaymentStatus getPaymentStatusRandomSampleGenerator() {
        return new PaymentStatus().id(longCount.incrementAndGet()).statusName(UUID.randomUUID().toString());
    }
}
