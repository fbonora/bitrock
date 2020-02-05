package com.bitrock.goose.engine.field;

import java.util.function.Function;

public class DefaultCell implements Cell {

    private final String name;

    private final Integer index;

    public DefaultCell(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Function<Integer, Integer> getCellRule() {
        return (roll) -> index;
    }

    public Integer getIndex() {
        return index;
    }

}
