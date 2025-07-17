package de.mlehrke.deathswap.command;

import de.mlehrke.deathswap.DeathSwapPlugin;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.game.state.states.InGameState;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SwapNowCommand implements CommandExecutor {

    private final DeathSwapPlugin plugin;
    private final GameStateContext context;

    public SwapNowCommand(DeathSwapPlugin plugin, GameStateContext context) {
        this.plugin = plugin;
        this.context = context;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(context.currentState() instanceof InGameState)) {
            commandSender.sendMessage(Component.text("§cDer Command kann nur in der Ingame Phase ausgeführt werden!"));
            return true;
        }
        if (commandSender.hasPermission("deathswap.swapnow")) {
            plugin.swapper().task().cancel();
            plugin.swapper().startCountdown();
        } else {
            commandSender.sendMessage(Component.text("§cDu hast keine berechtigung für diesen Command!"));
        }
        return true;
    }
}
