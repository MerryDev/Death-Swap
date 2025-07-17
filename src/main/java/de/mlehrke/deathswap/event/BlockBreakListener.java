package de.mlehrke.deathswap.event;

import de.mlehrke.deathswap.DeathSwapPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockBreakListener implements Listener {

    private final DeathSwapPlugin plugin;
    private final List<Material> allItems;

    public BlockBreakListener(DeathSwapPlugin plugin) {
        this.plugin = plugin;
        this.allItems = getAllItems();
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        event.setDropItems(false);

        List<ItemStack> randomizedDrops = getRandomizedDrops(block);
        randomizedDrops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation(), drop));
    }

    private List<ItemStack> getRandomizedDrops(Block block) {
        Random random = ThreadLocalRandom.current();

        Collection<ItemStack> originalDrops = block.getDrops();
        List<ItemStack> randomized = new ArrayList<>();

        for (ItemStack original : originalDrops) {
            Material randomMaterial = allItems.get(random.nextInt(allItems.size()));
            randomized.add(new ItemStack(randomMaterial, original.getAmount()));
        }
        return randomized;
    }

    private List<Material> getAllItems() {
        return Stream.of(Material.values())
                .filter(Material::isItem)
                .filter(this::isNotArmor)
                .filter(material -> material != Material.TOTEM_OF_UNDYING)
                .filter(material -> material != Material.KNOWLEDGE_BOOK)
                .filter(material -> material != Material.WRITTEN_BOOK)
                .filter(material -> material != Material.ENCHANTED_BOOK)
                .filter(material -> material != Material.GOAT_HORN)
                .filter(material -> material != Material.JIGSAW)
                .filter(material -> material != Material.DEBUG_STICK)
                .filter(material -> material != Material.LIGHT)
                .filter(material -> !material.name().contains("POTTERY_SHERD"))
                .filter(material -> !material.name().contains("DISC"))
                .filter(material -> !material.name().contains("LEGACY"))
                .filter(material -> !material.name().contains("COMMAND"))
                .filter(material -> !material.name().contains("STRUCTURE"))
                .filter(material -> !material.name().contains("BED"))
                .filter(material -> !material.name().contains("POTION"))
                .filter(material -> !material.name().contains("TIPPED_ARROW"))
                .filter(material -> !material.name().contains("TEMPLATE"))
                .filter(material -> !material.name().contains("PATTERN"))
                .collect(Collectors.toList());
    }

    private boolean isNotArmor(Material material) {
        return !(material.name().contains("CHESTPLATE") ||
                material.name().contains("LEGGINGS") ||
                material.name().contains("BOOTS") ||
                material.name().contains("HELMET")
        );
    }

    public void registerEvent() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
