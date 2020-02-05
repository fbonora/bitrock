package com.bitrock.goose.cli;

import com.bitrock.goose.engine.GameActions;
import com.bitrock.goose.engine.field.Cell;

import java.io.PrintStream;

public class CLIGameListener implements GameActions {

    private final PrintStream printStream;

    public CLIGameListener(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void onPlayerMoved(String playerName, Cell from, Cell to) {
        printStream.print(playerName
                + " moves from "
                + from.getName()
                + " to " + to.getName() + ". ");
    }

    @Override
    public void onPlayerBounced(String playerName, Cell to) {
        printStream.print(playerName + " bounces! " + playerName + " returns to " + to.getName() + ". ");
    }

    @Override
    public void onPlayerWin(String playerName) {
        printStream.print(playerName + " Wins!!");
    }

    @Override
    public void onPlayerJump(String playerName, Cell to) {
        printStream.print(playerName + " jumps to " + to.getName() + ". ");
    }

    @Override
    public void onPlayerPrank(String playerJokedName, Cell from, Cell to) {
        printStream.print("On " + from.getName() + " there is " + playerJokedName + ", who returns to " + to.getName() + ". ");
    }
}
