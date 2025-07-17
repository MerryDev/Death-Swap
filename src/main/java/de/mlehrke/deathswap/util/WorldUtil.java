package de.mlehrke.deathswap.util;

import de.mlehrke.deathswap.DeathSwapPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Random;

public class WorldUtil {

    private final DeathSwapPlugin plugin;
    private final Random random;
    public WorldUtil(DeathSwapPlugin plugin, Random random) {
        this.plugin = plugin;
        this.random = random;
    }

    public void createWorlds() {
        plugin.getLogger().info("Generating game Worlds...");
        createNormalWorld();
        createNetherWorld();
        createEndWorld();
        plugin.getLogger().info("All worlds generated.");
    }

    public void deleteWorld(@NotNull World world) {
        Bukkit.unloadWorld(world, false);
        File folder = new File(Bukkit.getWorldContainer(), world.getName());
        if(deleteWorldFolder(folder)) {
            plugin.getLogger().info("Welt " + world.getName() + " erfolgreich gelöscht.");
        } else {
            plugin.getLogger().info("Löschen der Welt " + world.getName() + " fehlgeschlagen.");
        }
    }

    private boolean deleteWorldFolder(File path) {
        if (!path.exists()) return false;
        new Thread(() -> {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorldFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }).start();
        return path.delete();
    }

    private void createNormalWorld() {
        plugin.getLogger().info("Generating Overworld...");
        WorldCreator creator = new WorldCreator("game");
        creator.environment(World.Environment.NORMAL);
        creator.type(WorldType.NORMAL);
        creator.seed(random.nextLong());
        creator.createWorld();
        plugin.getLogger().info("Done.");
    }

    private void createNetherWorld() {
        plugin.getLogger().info("Generating Nether...");
        WorldCreator creator = new WorldCreator("game_nether");
        creator.environment(World.Environment.NETHER);
        creator.type(WorldType.NORMAL);
        creator.seed(random.nextLong());
        creator.createWorld();
        plugin.getLogger().info("Done.");
    }

    private void createEndWorld() {
        plugin.getLogger().info("Generating End...");
        WorldCreator creator = new WorldCreator("game_the_end");
        creator.environment(World.Environment.THE_END);
        creator.type(WorldType.NORMAL);
        creator.seed(random.nextLong());
        creator.createWorld();
        plugin.getLogger().info("Done.");
    }
}
