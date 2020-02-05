package com.bitrock.goose.engine.field;

import java.util.function.Function;

public class TheGooseCell extends DefaultCell {

    public TheGooseCell(String name, Integer index) {
        super(name, index);
    }

    /*
     * 6. If you land on "The Goose", move again

		As a player, when I get to a space with a picture of "The Goose", I move forward again by the sum of the two dice rolled before
     * @see com.bitrock.goose.engine.field.DefaultCell#getCellRule()
     */
    @Override
    public Function<Integer, Integer> getCellRule() {
        return (Integer roll) -> getIndex() + roll;
    }
}
