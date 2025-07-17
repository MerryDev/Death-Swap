package de.mlehrke.deathswap.game.state;

import de.mlehrke.deathswap.DeathSwapPlugin;
import de.mlehrke.deathswap.game.state.states.InGameState;
import de.mlehrke.deathswap.game.state.states.LobbyState;
import de.mlehrke.deathswap.util.Timer;
import org.jetbrains.annotations.NotNull;

public class GameStateContext {

    private final GameState[] states;
    private GameState current;

    private final Timer timer;

    public GameStateContext(DeathSwapPlugin plugin) {
        this.timer = new Timer(plugin);
        this.states = new GameState[2];
        this.states[0] = new LobbyState(plugin, this);
        this.states[1] = new InGameState(plugin, this);
    }

    public void setGameState(int state) {
        stopCurrentState();
        this.current = this.states[state];
        this.current.start();
    }

    private void stopCurrentState() {
        if (this.current != null) {
            this.current.stop();
            this.current = null;
        }
    }

    public @NotNull GameState currentState() {
        return this.current;
    }

    public @NotNull Timer timer() {
        return timer;
    }
}
