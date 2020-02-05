package com.bitrock.goose.cli;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bitrock.goose.cli.exception.CommandNotFoundException;
import com.bitrock.goose.cli.exception.InvalidCommandParameterException;
import com.bitrock.goose.engine.Dice;
import com.bitrock.goose.engine.GooseGame;
import com.bitrock.goose.engine.exception.PlayerAlreadyExistsException;

public class CommandExecutorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private GooseGame gooseGame;
    private Dice dice;
    private PrintStream printStream;


    private CommandExecutor commandExecutor;

    @Before
    public void setUp() {
        gooseGame = mock(GooseGame.class);
        
        dice = mock(Dice.class);
        printStream = mock(PrintStream.class);

        commandExecutor = new CommandExecutor(gooseGame, dice, printStream);
    }

    @Test
    public void InvalidCommandThrowsException() throws Exception {
        expectedException.expect(CommandNotFoundException.class);
        commandExecutor.executeGameCommand("invalid command");
    }

    @Test
    public void executeAddPMissingPlayer() throws Exception {
    	expectedException.expect(InvalidCommandParameterException.class);
        commandExecutor.executeGameCommand("add player");

    }
    
    @Test
    public void executeAddPlayer() throws Exception {
        when(gooseGame.getPlayers()).thenReturn(Collections.singletonMap("Pippo", 0));

        commandExecutor.executeGameCommand("add player Pippo");

        verify(gooseGame).addPlayer("Pippo");
        verify(printStream).print("players: Pippo");
    }

    
    @Test
    public void executeAddDuplicatePlayerThrowsException() throws Exception {
    	expectedException.expect(PlayerAlreadyExistsException.class);
    	expectedException.expectMessage("Pippo: already existing player");
    	doThrow(new PlayerAlreadyExistsException("Pippo")).when(gooseGame).addPlayer("Pippo");

		commandExecutor.executeGameCommand("add player Pippo");

    }
    
    @Test
    public void executeMoveEmpty() throws Exception {
    	expectedException.expect(InvalidCommandParameterException.class);
        commandExecutor.executeGameCommand("move");

    }
    @Test
    public void executeMovePlayerWithDice() throws Exception {
        when(dice.roll()).thenReturn(4, 2);

        commandExecutor.executeGameCommand("move Pippo");

        verify(gooseGame).movePlayer("Pippo", Arrays.asList(4, 2));
        verify(printStream).print("Pippo rolls 4, 2. ");
    }
    
    @Test
    public void executeMovePlayerWithValues() throws Exception {
        commandExecutor.executeGameCommand("move Pippo 3, 4");

        verify(gooseGame).movePlayer("Pippo", Arrays.asList(3, 4));
        verify(printStream).print("Pippo rolls 3, 4. ");
        
    }
}