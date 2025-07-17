package de.mlehrke.deathswap.util;

import de.mlehrke.deathswap.DeathSwapPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class Timer {

    private final DeathSwapPlugin plugin;
    private boolean paused;
    private int time;

    public Timer(DeathSwapPlugin plugin) {
        this.plugin = plugin;
        this.paused = false;
        this.time = 0;
    }

    public void start() {
        if (paused) return;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            time++;

            String readableTime = formatTime();
            for (var player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(Component.text(readableTime, NamedTextColor.GOLD));
            }

        }, 0L, 20L);
    }

    public void reset() {
        paused(true);
        this.time = 0;
    }

    public void paused(boolean paused) {
        this.paused = paused;
    }

    private @NotNull String formatTime() {
        long days = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = (time % (60 * 60)) / 60;
        long seconds = time % 60;

        StringBuilder builder = new StringBuilder();
        if (days > 0) builder.append(days).append("d ");
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds > 0 || builder.isEmpty()) builder.append(seconds).append("s ");

        return builder.toString().trim();
    }

}
