package com.bitrock.goose.engine;

import java.util.Random;

public class Dice {

    private final Random random;
    private final int size;

    public Dice(int size) {
        this.size = size;
        this.random = new Random();
    }

    public Integer roll() {
        return random.ints(1, size + 1)
                .limit(1)
                .findFirst()
                .getAsInt();
    }
}
