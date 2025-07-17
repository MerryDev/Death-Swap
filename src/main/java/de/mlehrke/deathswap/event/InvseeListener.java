package de.mlehrke.deathswap.event;

import de.mlehrke.deathswap.command.InvseeCommand;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.game.state.states.InGameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class InvseeListener implements Listener {

    private final InvseeCommand invseeCommand;
    private final GameStateContext context;

    public InvseeListener(InvseeCommand invseeCommand, GameStateContext context) {
        this.invseeCommand = invseeCommand;
        this.context = context;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(this.context.currentState() instanceof InGameState)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        UUID uuid = player.getUniqueId();
        if (invseeCommand.readOnlyViewers().contains(uuid)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(this.context.currentState() instanceof InGameState)) return;
        if (!(event.getPlayer() instanceof Player player)) return;

        UUID uuid = player.getUniqueId();
        invseeCommand.readOnlyViewers().remove(uuid);
    }
}
