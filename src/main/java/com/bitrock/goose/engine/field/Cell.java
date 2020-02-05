package com.bitrock.goose.engine.field;

import java.util.function.Function;

public interface Cell {

    String getName();

    Function<Integer, Integer> getCellRule();
}
