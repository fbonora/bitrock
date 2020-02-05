package com.bitrock.goose.engine;

import com.bitrock.goose.engine.exception.PlayerAlreadyExistsException;
import com.bitrock.goose.engine.exception.PlayerNotFoundException;
import com.bitrock.goose.engine.field.Cell;

import java.util.*;

public class GooseGame {

    public static final int FINAL_CELL = 63;
    //Map k name, V position
    private final Map<String, Integer> players;
    //List of action listeners
    private final List<GameActions> gameListeners;
    private final TableField tableField;

    public GooseGame(TableField tableField) {
        this.tableField = tableField;
        this.players = new HashMap<>();
        this.gameListeners = new ArrayList<>();
    }

    public void addPlayer(String name) throws PlayerAlreadyExistsException {
        if (players.containsKey(name)) {
        	//Player already present in the game
            throw new PlayerAlreadyExistsException(name);
        }
        players.put(name, 0);
    }

    public void movePlayer(String playerName, List<Integer> rolls) throws PlayerNotFoundException {
        Integer actualCellIndex = players.get(playerName);
        if (actualCellIndex == null) {
            throw new PlayerNotFoundException(playerName);
        }

        Integer rollsSum = rolls.stream()
                .mapToInt(Integer::intValue)
                .sum();

        Integer newCellIndex = actualCellIndex + rollsSum;

        Cell actualCell = tableField.getCell(actualCellIndex);
        notifyAllOnPlayerMoved(playerName, actualCell, tableField.getCell(Math.min(newCellIndex, FINAL_CELL)));

        newCellIndex = evaluateCellRule(playerName, rollsSum, newCellIndex);

        handleAnotherPlayerInSameCell(playerName, actualCellIndex, newCellIndex);
    }

    /*
     * 7. Prank (Optional Step)

		As a player, when I land on a space occupied by another player, I send him to my previous position 
     */
    private void handleAnotherPlayerInSameCell(String actualPlayerName, Integer actualPlayerCell, Integer cellIndex) {
        players.entrySet().stream()
                .filter(e -> !e.getKey().equals(actualPlayerName))  //Not same player
                .filter(e -> e.getValue().equals(cellIndex))		//Same cell
                .forEach(e -> {										//Move matching players
                    e.setValue(actualPlayerCell);
                    notifyAllOnPlayerPrank(e.getKey(), tableField.getCell(cellIndex), tableField.getCell(actualPlayerCell));
                });
    }

    private Integer evaluateCellRule(String playerName, Integer rollsSum, Integer newSpaceIndex) {

        if (newSpaceIndex > FINAL_CELL) {
            newSpaceIndex = 2 * FINAL_CELL - newSpaceIndex;
            notifyAllOnBouncedPlayer(playerName, tableField.getCell(newSpaceIndex));
        } else if (newSpaceIndex == FINAL_CELL) {
            notifyAllOnPlayerWin(playerName);
        }

        Integer evaluatedLandingCellRule = tableField.getCell(newSpaceIndex).getCellRule().apply(rollsSum);
        if (!evaluatedLandingCellRule.equals(newSpaceIndex)) {
            notifyAllOnPlayerJump(playerName, tableField.getCell(evaluatedLandingCellRule));
            return evaluateCellRule(playerName, rollsSum, evaluatedLandingCellRule);
        }

        players.put(playerName, evaluatedLandingCellRule);

        return evaluatedLandingCellRule;
    }

    public void addGameListener(GameActions gameListener) {
        gameListeners.add(gameListener);
    }

    public Map<String, Integer> getPlayers() {
        return Collections.unmodifiableMap(players);
    }

    private void notifyAllOnPlayerMoved(String playerName, Cell from, Cell to) {
        gameListeners.forEach(gameListener -> gameListener.onPlayerMoved(playerName, from, to));
    }

    private void notifyAllOnBouncedPlayer(String playerName, Cell to) {
        gameListeners.forEach(gameListener -> gameListener.onPlayerBounced(playerName, to));
    }

    private void notifyAllOnPlayerWin(String playerName) {
        gameListeners.forEach(gameListener -> gameListener.onPlayerWin(playerName));
    }

    private void notifyAllOnPlayerJump(String playerName, Cell to) {
        gameListeners.forEach(gameListener -> gameListener.onPlayerJump(playerName, to));
    }

    private void notifyAllOnPlayerPrank(String playerJokedName, Cell from, Cell to) {
        gameListeners.forEach(gameListener -> gameListener.onPlayerPrank(playerJokedName, from, to));
    }
}
