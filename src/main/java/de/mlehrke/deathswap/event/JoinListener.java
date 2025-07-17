package de.mlehrke.deathswap.event;

import de.mlehrke.deathswap.DeathSwapPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener {

    private final DeathSwapPlugin plugin;

    public JoinListener(DeathSwapPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        World world = Bukkit.getWorlds().getFirst();
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(world.getSpawnLocation()), 5);
        if(player.getGameMode() == GameMode.SPECTATOR) return;
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        ItemStack steak = new ItemStack(Material.COOKED_BEEF);
        steak.setAmount(64);
        player.getInventory().addItem(steak);
    }

    public void registerEvent() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
