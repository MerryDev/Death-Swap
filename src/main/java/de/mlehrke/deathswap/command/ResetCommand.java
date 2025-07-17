package de.mlehrke.deathswap.command;

import de.mlehrke.deathswap.DeathSwapPlugin;
import de.mlehrke.deathswap.game.state.GameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.game.state.states.InGameState;
import de.mlehrke.deathswap.util.WorldUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ResetCommand implements CommandExecutor {

    private final DeathSwapPlugin plugin;
    private final GameStateContext context;

    public ResetCommand(DeathSwapPlugin plugin, GameStateContext context) {
        this.plugin = plugin;
        this.context = context;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Component.text("§cNur Spieler können diesen Befehl nutzen."));
            return true;
        }
        if (!(context.currentState() instanceof InGameState)) {
            player.sendMessage(Component.text("§cDieser befehl steht nur Ingame zur verfügung!"));
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(Component.text("§cBenutzung: /reset"));

        }

        if (!player.hasPermission("deathswap.reset")) {
            commandSender.sendMessage(Component.text("§cDu hast keine berechtigung für diesen Command!"));
            return true;
        }
        player.sendMessage(Component.text("§cDas Spiel wird zurückgesetzt. Bitte warten."));
        World pregameWorld = Bukkit.getWorld("pregame");
        player.teleport(pregameWorld.getSpawnLocation());
        context.setGameState(GameState.LOBBY);

        WorldUtil wu = new WorldUtil(plugin, new Random());
        List<World> worlds =List.of(
                Objects.requireNonNull(Bukkit.getWorld("game")),
                Objects.requireNonNull(Bukkit.getWorld("game_nether")),
                Objects.requireNonNull(Bukkit.getWorld("game_the_end"))
        );
        for (World world : worlds) {
            if (world == null) continue;
            wu.deleteWorld(world);
        }
        wu.createWorlds();
        player.sendMessage(Component.text("§aDas Spiel wurde zurückgesetzt."));
        return true;
    }
}
