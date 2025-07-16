import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeathSwapPlugin extends JavaPlugin implements Listener {

    private List<Material> allItems;

    @Override
    public void onEnable() {
        new PlayerSwapper(this).start();
        allItems = getAllItems();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("DeathSwapPlugin enabled!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Standarddrops verhindern
        event.setDropItems(false);

        // Drops randomisieren
        List<ItemStack> randomizedDrops = getRandomizedDrops(block);

        // Randomized Drops droppen
        randomizedDrops.forEach(drop ->
                block.getWorld().dropItemNaturally(block.getLocation(), drop));
    }

    private List<ItemStack> getRandomizedDrops(Block block) {
        List<Material> possibleItems = getAllItems();
        Random random = ThreadLocalRandom.current();

        Collection<ItemStack> originalDrops = block.getDrops();
        List<ItemStack> randomized = new ArrayList<>();

        for (ItemStack original : originalDrops) {
            Material randomMaterial = possibleItems.get(random.nextInt(possibleItems.size()));
            randomized.add(new ItemStack(randomMaterial, original.getAmount()));
        }
        return randomized;
    }


    private List<Material> getAllItems() {
        return Stream.of(Material.values())
                .filter(Material::isItem)
                .filter(material -> !material.name().contains("LEGACY"))
                .filter(material -> !material.name().contains("COMMAND"))
                .filter(material -> !material.name().contains("STRUCTURE"))
                .collect(Collectors.toList());
    }

}

