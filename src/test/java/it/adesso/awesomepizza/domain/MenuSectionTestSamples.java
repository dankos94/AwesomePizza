package it.adesso.awesomepizza.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MenuSectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MenuSection getMenuSectionSample1() {
        return new MenuSection().id(1L).name("name1").description("description1");
    }

    public static MenuSection getMenuSectionSample2() {
        return new MenuSection().id(2L).name("name2").description("description2");
    }

    public static MenuSection getMenuSectionRandomSampleGenerator() {
        return new MenuSection()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
