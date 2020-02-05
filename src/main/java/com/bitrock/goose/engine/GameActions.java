package com.bitrock.goose.engine;

import com.bitrock.goose.engine.field.Cell;

public interface GameActions {

    void onPlayerMoved(String playerName, Cell from, Cell to);

    void onPlayerBounced(String playerName, Cell to);

    void onPlayerWin(String playerName);

    void onPlayerJump(String playerName, Cell to);

    void onPlayerPrank(String playerJokedName, Cell from, Cell to);
}
