import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerSwapper {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public PlayerSwapper(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        scheduleNextSwap();
    }

    private void scheduleNextSwap() {
        int minTicks = 7 * 60 * 20;
        int maxTicks = 10 * 60 * 20;
        int delay = minTicks + random.nextInt(maxTicks - minTicks + 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                startCountdown();
            }
        }.runTaskLater(plugin, delay);
    }

    private void startCountdown() {
        new BukkitRunnable() {
            int seconds = 10;

            @Override
            public void run() {
                if (seconds > 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("§cSwap in", "§e" + seconds, 0, 20, 0);
                    }
                    seconds--;
                } else {
                    this.cancel();
                    swapPlayers();
                    scheduleNextSwap();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20L = 1 Sekunde
    }

    private void swapPlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        if (players.size() < 2) return;

        // Positionen merken
        List<Location> locations = players.stream()
                .map(Player::getLocation)
                .toList();

        // Durchmischen
        Collections.shuffle(players, random);

        // Teleportieren im Ring
        for (int i = 0; i < players.size(); i++) {
            Player current = players.get(i);
            Location targetLoc = locations.get((i + 1) % players.size());
            current.teleport(targetLoc);
        }

        Bukkit.broadcastMessage("§aAlle Spieler wurden zufällig geswapped!");
    }
}
