package de.mlehrke.deathswap.game.state;

public abstract class AbstractGameState implements GameState {

    protected final GameStateContext context;

    public AbstractGameState(GameStateContext context) {
        this.context = context;
    }
}
