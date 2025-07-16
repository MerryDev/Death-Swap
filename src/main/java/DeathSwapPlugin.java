import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        Block block = event.getBlock();

        List<ItemStack> drops = new ArrayList<>(block.getDrops());
        if (drops.isEmpty()) return;

        block.getDrops().clear();
        List<ItemStack> newDrops = new ArrayList<>();
        for (ItemStack drop : drops) {
            if (!drop.getType().isItem()) continue;
            ItemStack randomized = ItemStack.of(allItems.get(new Random().nextInt(allItems.size() - 1)), drop.getAmount());

            newDrops.add(randomized);
        }

        block.getDrops().addAll(newDrops);
    }

    private List<Material> getAllItems() {
        return Stream.of(Material.values())
                .filter(Material::isItem)
                .collect(Collectors.toList());
    }

}

