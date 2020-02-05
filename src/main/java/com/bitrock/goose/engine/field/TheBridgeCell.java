package com.bitrock.goose.engine.field;

import java.util.function.Function;

public class TheBridgeCell extends DefaultCell {

    public TheBridgeCell(String name, Integer index) {
        super(name, index);
    }

    @Override
    public Function<Integer, Integer> getCellRule() {
        return (roll) -> 12;
    }
}
