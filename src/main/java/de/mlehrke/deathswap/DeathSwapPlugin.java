package de.mlehrke.deathswap;

import de.mlehrke.deathswap.command.InvseeCommand;
import de.mlehrke.deathswap.command.StartCommand;
import de.mlehrke.deathswap.event.InvseeListener;
import de.mlehrke.deathswap.game.state.GameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class DeathSwapPlugin extends JavaPlugin implements Listener {

    private GameStateContext context;
    private InvseeCommand invseeCommand;

    @Override
    public void onEnable() {
        context = new GameStateContext(this);
        invseeCommand = new InvseeCommand(context);
        registerCommands();
        registerGameMechanics();
        getLogger().info("DeathSwapPlugin enabled!");
    }

    private void registerGameMechanics() {
        Bukkit.getPluginManager().registerEvents(new InvseeListener(invseeCommand, context), this);
        context.setGameState(GameState.LOBBY);
    }

    private void registerCommands() {
        getCommand("start").setExecutor(new StartCommand(context));
        getCommand("invsee").setExecutor(invseeCommand);
    }
}

