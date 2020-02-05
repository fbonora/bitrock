package com.bitrock.goose.cli.exception;

@SuppressWarnings("serial")
public class CommandNotFoundException extends Exception {

    public CommandNotFoundException(String userString) {
        super("Command " + userString + " not found");
    }
}
