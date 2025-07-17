package de.mlehrke.deathswap.command;

import de.mlehrke.deathswap.DeathSwapPlugin;
import de.mlehrke.deathswap.util.PlayerSwapper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    private final DeathSwapPlugin plugin;

    public StartCommand(DeathSwapPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        new PlayerSwapper(plugin).start();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setInvulnerable(false);
        }
        World world = Bukkit.getWorlds().getFirst();
        world.getWorldBorder().reset();
        sender.sendMessage("§aSwapping gestartet!");
        return true;
    }
}
