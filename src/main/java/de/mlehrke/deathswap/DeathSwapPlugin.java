package de.mlehrke.deathswap;

import de.mlehrke.deathswap.command.StartCommand;
import de.mlehrke.deathswap.event.BlockBreakListener;
import de.mlehrke.deathswap.event.JoinListener;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class DeathSwapPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        World world = Bukkit.getWorlds().getFirst();
        world.getWorldBorder().setCenter(world.getSpawnLocation());
        world.getWorldBorder().setSize(20);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

        registerEvents();
        registerCommands();
        getLogger().info("DeathSwapPlugin enabled!");
    }

    private void registerEvents() {
        BlockBreakListener bl = new BlockBreakListener(this);
        JoinListener jl = new JoinListener(this);
        bl.registerEvent();
        jl.registerEvent();
    }

    private void registerCommands() {
        StartCommand startCommand = new StartCommand(this);
        getCommand("start").setExecutor(startCommand);

    }
}

