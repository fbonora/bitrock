package com.bitrock.goose.engine;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bitrock.goose.engine.exception.PlayerAlreadyExistsException;
import com.bitrock.goose.engine.exception.PlayerNotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GooseGameTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private GameActions gameListener;

    private GooseGame gooseGame;
    private TableField tableField;

    @Before
    public void setUp() {
        tableField = new TableField();

        gameListener = mock(GameActions.class);

        gooseGame = new GooseGame(tableField);
        gooseGame.addGameListener(gameListener);
    }

    @Test
    public void testAddPlayer() throws Exception {
        String playerName = "Pippo";

        gooseGame.addPlayer(playerName);

        assertThat(gooseGame.getPlayers(), hasEntry(playerName, 0));
        verifyZeroInteractions(gameListener);
    }
    
    
    @Test
    public void testAddExistingPlayer() throws Exception {
        String playerName = "Pippo";

        gooseGame.addPlayer(playerName);

        expectedException.expect(PlayerAlreadyExistsException.class);
        gooseGame.addPlayer(playerName);
    }
    
    
    @Test
    public void testMoveUnexistingPlayer() throws Exception {
        String playerName = "invalid";

        expectedException.expect(PlayerNotFoundException.class);
        gooseGame.movePlayer(playerName, Arrays.asList(4, 2));
        
    }

    @Test
    public void testMovePlayerOnDefaultCell() throws Exception {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);

        gooseGame.movePlayer(playerName, Arrays.asList(2, 2));

        verify(gameListener).onPlayerMoved(playerName, tableField.getCell(0), tableField.getCell(4));
        assertThat(gooseGame.getPlayers().get(playerName), equalTo(4));
    }

    @Test
    public void testMovePlayerOnBridgeCell() throws Exception {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);

        gooseGame.movePlayer(playerName, Arrays.asList(4, 2));

        verify(gameListener).onPlayerMoved(playerName, tableField.getCell(0), tableField.getCell(6));
        verify(gameListener).onPlayerJump(playerName, tableField.getCell(12));
        assertThat(gooseGame.getPlayers().get(playerName), equalTo(12));
    }

    @Test
    public void testMovePlayerToWin() throws Exception {
        String playerName = "Pippo"; 
        
        gooseGame.addPlayer(playerName);
        gooseGame.movePlayer(playerName, Arrays.asList(60, 3));

        verify(gameListener).onPlayerMoved(playerName, tableField.getCell(0), tableField.getCell(63));
        verify(gameListener).onPlayerWin(playerName);
        assertThat(gooseGame.getPlayers().get(playerName), equalTo(63));
    }

    @Test
    public void testMovePlayerOverMaxSpaceBounceAndUpdatePlayerSpaceAndNotifyBounced() throws Exception {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);

        gooseGame.movePlayer(playerName, Arrays.asList(60, 5));

        verify(gameListener).onPlayerMoved(playerName, tableField.getCell(0), tableField.getCell(63));
        verify(gameListener).onPlayerBounced(playerName, tableField.getCell(61));
        verifyNoMoreInteractions(gameListener);
        assertThat(gooseGame.getPlayers().get(playerName), equalTo(61));
    }

    @Test
    public void testMovePlayerOnGooseCell_14() throws Exception {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);

        gooseGame.movePlayer(playerName, Arrays.asList(6, 6));
        gooseGame.movePlayer(playerName, Arrays.asList(1, 1));

        verify(gameListener).onPlayerMoved(playerName, tableField.getCell(0), tableField.getCell(12));
        verify(gameListener).onPlayerJump(playerName, tableField.getCell(16));
        assertThat(gooseGame.getPlayers().get(playerName), equalTo(16));
    }
    
    @Test
    public void testMovePlayerOnGooseCell() throws Exception {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);

        gooseGame.movePlayer(playerName, Arrays.asList(4, 1));

        verify(gameListener).onPlayerMoved(playerName, tableField.getCell(0), tableField.getCell(5));
        assertThat(gooseGame.getPlayers().get(playerName), equalTo(10));
    }

    @Test
    public void testMovePlayerOnGooseCellWithMultipleJump() throws Exception {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);

        gooseGame.movePlayer(playerName, Arrays.asList(4, 6));
        gooseGame.movePlayer(playerName, Arrays.asList(2, 2));

        verify(gameListener).onPlayerMoved(playerName, tableField.getCell(0), tableField.getCell(10));
        verify(gameListener).onPlayerJump(playerName, tableField.getCell(22));
        assertThat(gooseGame.getPlayers().get(playerName), equalTo(22));
    }

    @Test
    public void testMovePlayerOnAlreadyOccupiedSpaceUpdateTwoPlayer() throws Exception {
        String playerName1 = "Pippo";
        gooseGame.addPlayer(playerName1);
        gooseGame.movePlayer(playerName1, Collections.singletonList(15));

        String playerName2 = "Pluto";
        gooseGame.addPlayer(playerName2);
        gooseGame.movePlayer(playerName2, Collections.singletonList(17));

        gooseGame.movePlayer(playerName1, Arrays.asList(1, 1));

        verify(gameListener).onPlayerMoved(playerName1, tableField.getCell(15), tableField.getCell(17));
        verify(gameListener).onPlayerPrank(playerName2, tableField.getCell(17), tableField.getCell(15));
        assertThat(gooseGame.getPlayers().get(playerName1), equalTo(17));
        assertThat(gooseGame.getPlayers().get(playerName2), equalTo(15));
    }
}