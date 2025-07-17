package de.mlehrke.deathswap.command;

import de.mlehrke.deathswap.game.state.GameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.game.state.states.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    private final GameStateContext context;

    public StartCommand(GameStateContext context) {
        this.context = context;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(context.currentState() instanceof LobbyState)) return true;
        if (Bukkit.getOnlinePlayers().size() < 2) return true;
        context.setGameState(GameState.INGAME);
        sender.sendMessage("§aSwapping gestartet!");
        return true;
    }
}
