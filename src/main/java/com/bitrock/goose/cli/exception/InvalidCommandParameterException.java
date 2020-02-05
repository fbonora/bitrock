package com.bitrock.goose.cli.exception;

@SuppressWarnings("serial")
public class InvalidCommandParameterException extends Exception {

    public InvalidCommandParameterException(String userString) {
        super("Command " + userString + " invalid parameters");
    }
}
