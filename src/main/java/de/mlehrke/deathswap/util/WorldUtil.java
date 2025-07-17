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

        Bukkit.getScheduler().runTask(plugin, () -> createWorld("game", World.Environment.NORMAL));
        Bukkit.getScheduler().runTaskLater(plugin, () -> createWorld("game_nether", World.Environment.NETHER), 20L);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            createWorld("game_the_end", World.Environment.THE_END);
            plugin.getLogger().info("All worlds generated.");
        }, 40L);

    }

    public void deleteWorld(@NotNull World world) {
        String name = world.getName();

        boolean unloaded = Bukkit.unloadWorld(world, false);
        if (!unloaded) {
            plugin.getLogger().warning("Welt '" + name + "' konnte nicht entladen werden.");
            return;
        }

        // 5 Sekunden warten, damit alles entkoppelt ist
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            File folder = new File(Bukkit.getWorldContainer(), name);
            if (deleteWorldFolder(folder)) {
                plugin.getLogger().info("Welt '" + name + "' erfolgreich gelöscht.");
            } else {
                plugin.getLogger().warning("Löschen der Welt '" + name + "' fehlgeschlagen.");
            }
        }, 100L); // 100 Ticks = 5 Sekunden
    }

    private boolean deleteWorldFolder(File path) {
        if (!path.exists()) return false;
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
        return path.delete();
    }

    private void createWorld(String name, World.Environment env) {
        plugin.getLogger().info("Generating " + env.name() + "...");
        WorldCreator creator = new WorldCreator(name);
        creator.environment(env);
        creator.type(WorldType.NORMAL);
        creator.seed(random.nextLong());
        creator.createWorld();
        plugin.getLogger().info("Done.");
    }
}
