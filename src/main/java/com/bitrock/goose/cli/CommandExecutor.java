package com.bitrock.goose.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bitrock.goose.cli.exception.CommandNotFoundException;
import com.bitrock.goose.cli.exception.InvalidCommandParameterException;
import com.bitrock.goose.engine.Dice;
import com.bitrock.goose.engine.GooseGame;
import com.bitrock.goose.engine.exception.PlayerAlreadyExistsException;
import com.bitrock.goose.engine.exception.PlayerNotFoundException;

public class CommandExecutor {

    private final GooseGame gooseGame;
    private final Dice dice;

    private final PrintStream printStream;
	private final String ADD_PLAYER = "add player";
	private final String MOVE = "move";
	private final String EXIT = "exit";
	private final String[] goodCmds = {ADD_PLAYER, MOVE, EXIT};

    public CommandExecutor(GooseGame gooseGame, Dice dice, PrintStream printStream) {
        this.gooseGame = gooseGame;
        this.dice = dice;
        this.printStream = printStream;
    }

    public void executeGameCommand(String userString) throws Exception {
    	List<String> cmdAndArgs = getCommandArgs(userString);
        switch (cmdAndArgs.get(0)) {
            case "add":
                handleAddPlayer(cmdAndArgs.get(cmdAndArgs.size()-1));
                break;
            case "move":
                handleMovePlayer(cmdAndArgs.subList(1, cmdAndArgs.size()));
                break;
            default:
                throw new CommandNotFoundException(userString);
        }
    }

    private void handleMovePlayer(List<String> userArguments) throws PlayerNotFoundException {
    	String playerName = userArguments.get(0);
    	List<Integer> rolls;
    	if(userArguments.size()== 3) {
    		Integer roll1 = Integer.valueOf(userArguments.get(1));
    		Integer roll2 = Integer.valueOf(userArguments.get(2));
            rolls = Arrays.asList(roll1, roll2);
    	}
    	else {
    		rolls = Arrays.asList(dice.roll(), dice.roll());
    	}

        hadleMovePlayerWithValues(playerName, rolls);
    }

	private void hadleMovePlayerWithValues(String playerName, List<Integer> values) throws PlayerNotFoundException {
		String rollsString = values.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        printStream.print(playerName + " rolls " + rollsString + ". " );

        gooseGame.movePlayer(playerName, values);
	}

    private void handleAddPlayer(String user) throws PlayerAlreadyExistsException {
        gooseGame.addPlayer(user);

        printStream.print(gooseGame.getPlayers().keySet()
                .stream()
                .collect(Collectors.joining(", ", "players: ", "")));
    }

	public List<String> getCommandArgs(String fullCmd) throws CommandNotFoundException, InvalidCommandParameterException {
		List<String> retVal = new ArrayList<String>();
		
		Arrays.stream(goodCmds)
        .filter(c -> fullCmd.startsWith(c))
        .findFirst()
        .orElseThrow(() -> new CommandNotFoundException(fullCmd));
		
		if(fullCmd.startsWith(EXIT)) {
			System.out.println("The Goose Game terminated, Bye!");
			System.exit(0);
		}
		else if (fullCmd.startsWith(ADD_PLAYER)) {
			retVal = Arrays.asList(fullCmd.split(" "));
			if(retVal.size()< 3) {
				throw new InvalidCommandParameterException(fullCmd);
			}
		}
		else {
			//Case move, manage move <player> or move <player> x, y
			retVal = Arrays.asList(fullCmd.split(" "));
			if(retVal.size() < 2) {
				throw new InvalidCommandParameterException(fullCmd);
			}
			if (retVal.size() > 2) {
				//case move <player> x, y  need to remove the ,
				retVal =retVal.stream().map(s -> s.replaceAll(",", "")).collect(Collectors.toList());
			}
		}
		
		return retVal ;
	}
}
