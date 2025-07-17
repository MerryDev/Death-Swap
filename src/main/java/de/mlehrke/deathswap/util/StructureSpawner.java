package de.mlehrke.deathswap.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.io.File;
import java.util.Random;

public class StructureSpawner {


    public static void spawnStructure(JavaPlugin plugin, String worldName, Location location) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("Welt '" + worldName + "' nicht gefunden!");
            return;
        }

        File structureFile = new File(plugin.getDataFolder(), "structure/pregame.nbt");

        try {
            StructureManager manager = Bukkit.getStructureManager();
            Structure structure = manager.loadStructure(structureFile);

            structure.place(
                    location,
                    false,
                    StructureRotation.NONE,
                    Mirror.NONE,
                    0,
                    1,
                    new Random()
            );

            plugin.getLogger().info("Structure wurde platziert.");
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Platzieren der Structure: " + e.getMessage());
            plugin.getLogger().severe(e.getMessage());
        }
        Location spawnLocation = new Location(world, 12.5, 94, 12.5);
        world.setSpawnLocation(spawnLocation);
    }
}
