package com.bitrock.goose.engine;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bitrock.goose.engine.field.DefaultCell;
import com.bitrock.goose.engine.field.Cell;
import com.bitrock.goose.engine.field.TheBridgeCell;
import com.bitrock.goose.engine.field.TheGooseCell;

public class TableField {

    private final List<Cell> cells;

    public TableField() {
        cells = IntStream.rangeClosed(0, GooseGame.FINAL_CELL)
                .mapToObj(i -> {
                    switch (i) {
                        case 0:
                            return new DefaultCell("Start", i);
                        //Space "6" is "The Bridge"    
                        case 6:
                            return new TheBridgeCell("The Bridge", i);
                        //The spaces 5, 9, 14, 18, 23, 27 have a picture of "The Goose"
                        case 5:
                        case 9:
                        case 14:
                        case 18:
                        case 23:
                        case 27:
                            return new TheGooseCell(i + ", The Goose", i);
                        default:
                            return new DefaultCell(Integer.toString(i), i);
                    }
                })
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public Cell getCell(Integer index) {
        return cells.get(index);
    }
}
