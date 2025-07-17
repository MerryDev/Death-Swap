package de.mlehrke.deathswap.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class VoidChunkGenerator extends ChunkGenerator {

    /**
     * Generates an empty world!
     *
     * @param worldInfo the world to generate chunks in
     * @param random    a pseudorandom number generator
     * @param x         the chunk's x coordinate
     * @param z         the chunk's z coordinate
     * @param chunkData the ChunkData being generated
     */

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, ChunkData chunkData) {
        chunkData.setRegion(0, chunkData.getMinHeight(), 0, 16, chunkData.getMaxHeight(), 16, Material.VOID_AIR);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    /**
     * Gets the fixed spawn location of a world.
     *
     * @param world  the world from which to get the spawn location
     * @param random a pseudorandom number generator
     * @return the spawn location of the world
     */
    @Override
    public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return new Location(world, 0, 70, 0);
    }
}

