package de.mlehrke.deathswap;

import de.mlehrke.deathswap.command.StartCommand;
import de.mlehrke.deathswap.game.state.GameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class DeathSwapPlugin extends JavaPlugin implements Listener {

    GameStateContext context = new GameStateContext(this);

    @Override
    public void onEnable() {
        registerCommands();
        registerGameState();
        getLogger().info("DeathSwapPlugin enabled!");
    }

    private void registerGameState() {
        context.setGameState(GameState.LOBBY);
    }

    private void registerCommands() {
        getCommand("start").setExecutor(new StartCommand(context));
    }
}

