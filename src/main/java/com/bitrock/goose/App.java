package com.bitrock.goose;

import com.bitrock.goose.cli.CLIGameListener;
import com.bitrock.goose.cli.CommandExecutor;
import com.bitrock.goose.cli.exception.CommandNotFoundException;
import com.bitrock.goose.cli.exception.GameStoppedException;
import com.bitrock.goose.cli.exception.InvalidCommandParameterException;
import com.bitrock.goose.engine.Dice;
import com.bitrock.goose.engine.GooseGame;
import com.bitrock.goose.engine.TableField;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello! This is The Goose Game!");

        GooseGame gooseGame = new GooseGame(new TableField());
        gooseGame.addGameListener(new CLIGameListener(System.out));

        Dice dice = new Dice(6);

        CommandExecutor commandExecutor = new CommandExecutor(gooseGame, dice, System.out);

        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.print("Type command: ");
                String command = scanner.nextLine();
                try {
                    commandExecutor.executeGameCommand(command);
                } catch (CommandNotFoundException | InvalidCommandParameterException cnfe) {
                    System.out.println(cnfe.getMessage());
                } catch (Exception e) {
                	 System.out.println(e.getMessage());
                }
                System.out.println();
            }
        }
    }
}
