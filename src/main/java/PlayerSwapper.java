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

        List<Location> originalLocations = players.stream()
                .map(Player::getLocation)
                .toList();

        // Erzeuge gültiges Derangement
        List<Integer> mapping = generateDerangement(players.size());

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Location targetLocation = originalLocations.get(mapping.get(i));
            player.teleport(targetLocation);
        }

        Bukkit.broadcastMessage("§aAlle Spieler wurden zufällig geswapped!");
    }

    private List<Integer> generateDerangement(int size) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) indices.add(i);

        while (true) {
            Collections.shuffle(indices, random);

            boolean deranged = true;
            for (int i = 0; i < size; i++) {
                if (indices.get(i) == i) {
                    deranged = false;
                    break;
                }
            }

            if (deranged) return indices;
        }
    }


}
