package de.mlehrke.deathswap.command;

import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.game.state.states.InGameState;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InvseeCommand implements CommandExecutor {

    private final GameStateContext context;
    private final Set<UUID> readOnlyViewers = new HashSet<>();

    public InvseeCommand(GameStateContext context) {
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

        if (args.length < 1) {
            player.sendMessage(Component.text("§cBenutzung: /invsee <spieler> [armor]"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("§cDieser Spieler ist entweder offline oder existiert nicht!"));
            return true;
        }

        if (player == target) {
            player.sendMessage("§aBitte versuche nicht dein eigenes Inventar zu öffnen...");
            return true;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("armor")) {
            Inventory armorView = Bukkit.createInventory(null, 9, Component.text("Armor: " + target.getName() + " (Schreibgeschützt)"));

            ItemStack[] armor = target.getInventory().getArmorContents();
            for (int i = 0; i < armor.length; i++) {
                armorView.setItem(i, armor[i]);
            }

            player.openInventory(armorView);
            readOnlyViewers.add(player.getUniqueId());
            return true;
        }
        player.openInventory(target.getInventory());
        if (!player.hasPermission("deathswap.invsee.edit")) {
            readOnlyViewers.add(player.getUniqueId());
        }
        return true;
    }

    public Set<UUID> readOnlyViewers() {
        return this.readOnlyViewers;
    }
}
