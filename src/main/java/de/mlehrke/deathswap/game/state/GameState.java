package de.mlehrke.deathswap.game.state;

public interface GameState {
    int LOBBY = 0, INGAME = 1;

    void start();

    void stop();
}
