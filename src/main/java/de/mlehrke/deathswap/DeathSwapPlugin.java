package de.mlehrke.deathswap;

import de.mlehrke.deathswap.command.InvseeCommand;
import de.mlehrke.deathswap.command.StartCommand;
import de.mlehrke.deathswap.command.SwapNowCommand;
import de.mlehrke.deathswap.event.InvseeListener;
import de.mlehrke.deathswap.game.state.GameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.util.PlayerSwapper;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public class DeathSwapPlugin extends JavaPlugin implements Listener {

    private GameStateContext context;
    private InvseeCommand invseeCommand;
    private PlayerSwapper swapper;

    @Override
    public void onEnable() {
        this.swapper = new PlayerSwapper(this);
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
        Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand(context));
        Objects.requireNonNull(getCommand("invsee")).setExecutor(invseeCommand);
        Objects.requireNonNull(getCommand("swapnow")).setExecutor(new SwapNowCommand(this, context));
    }

    public PlayerSwapper swapper() {
        return this.swapper;
    }

}

