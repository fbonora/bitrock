package com.bitrock.goose.engine.exception;

public class PlayerAlreadyExistsException extends Exception {
    public PlayerAlreadyExistsException(String name) {
        super( name + ": already existing player");
    }
}
