package de.mlehrke.deathswap;

import de.mlehrke.deathswap.command.InvseeCommand;
import de.mlehrke.deathswap.command.StartCommand;
import de.mlehrke.deathswap.command.SwapNowCommand;
import de.mlehrke.deathswap.event.InvseeListener;
import de.mlehrke.deathswap.game.state.GameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.util.PlayerSwapper;
import de.mlehrke.deathswap.util.StructureSpawner;
import de.mlehrke.deathswap.util.VoidChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public class DeathSwapPlugin extends JavaPlugin implements Listener {

    private GameStateContext context;
    private InvseeCommand invseeCommand;
    private PlayerSwapper swapper;

    @Override
    public void onEnable() {

        this.saveResource("structure/pregame.nbt", false);

        World world = createVoidWorld();
        Location structureLocation = new Location(world, 0,90,0);
        StructureSpawner.spawnStructure(this,"pregame", structureLocation);
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

    private World createVoidWorld() {
        WorldCreator creator = new WorldCreator("pregame");
        creator.generator(new VoidChunkGenerator());
        return creator.createWorld();
    }
}

