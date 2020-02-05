# Bitrock
Goose exercise for Bitrock s.r.l.

## Compile game

JDK version 1.8 and maven 3 are required to build the game.
To compile and build the project follow this steps:

1. `git clone` the project in a directory.
2. `mvn clean package` on the project directory.

## Play game

To start game go to **target** directory under project directory and execute `java -jar goose-game-1.0.jar`

### Game commands

The commands are:

- `add player <PLAYER_NAME>` : add player to game.
- `move <PLAYER_NAME>` : rolls two dice and move player.
- `move <PLAYER_NAME> <X>, <Y>` : move player of <X> + <Y> cells.
- `exit` : exit the game.

