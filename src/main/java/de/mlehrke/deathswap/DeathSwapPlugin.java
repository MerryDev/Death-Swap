package de.mlehrke.deathswap;

import de.mlehrke.deathswap.command.InvseeCommand;
import de.mlehrke.deathswap.command.ResetCommand;
import de.mlehrke.deathswap.command.StartCommand;
import de.mlehrke.deathswap.command.SwapNowCommand;
import de.mlehrke.deathswap.event.InvseeListener;
import de.mlehrke.deathswap.game.state.GameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.util.PlayerSwapper;
import de.mlehrke.deathswap.util.StructureSpawner;
import de.mlehrke.deathswap.util.VoidChunkGenerator;
import de.mlehrke.deathswap.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.Random;


public class DeathSwapPlugin extends JavaPlugin implements Listener {

    private GameStateContext context;
    private InvseeCommand invseeCommand;
    private PlayerSwapper swapper;

    @Override
    public void onEnable() {

        preparePregameLobby();
        prepareGameWorlds();

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
        Objects.requireNonNull(getCommand("reset")).setExecutor(new ResetCommand(this, context));
    }

    public PlayerSwapper swapper() {
        return this.swapper;
    }

    private World createVoidWorld() {
        WorldCreator creator = new WorldCreator("pregame");
        creator.generator(new VoidChunkGenerator());
        return creator.createWorld();
    }

    private void preparePregameLobby() {
        this.getLogger().info("Saving structure file...");
        File file = new File("structure/pregame.nbt");
        if (!file.exists()) {
            this.saveResource("structure/pregame.nbt", false);
            this.getLogger().info("Structure file saved.");
        } else {
            this.getLogger().info("Skipping. Structure file exists.");
        }

        World pregameWorld = createVoidWorld();
        Location structureLocation = new Location(pregameWorld, 0, 90, 0);
        StructureSpawner.spawnStructure(this, "pregame", structureLocation);
    }
    private void prepareGameWorlds() {
        WorldUtil wu = new WorldUtil(this, new Random());
        wu.createWorlds();
    }
}

