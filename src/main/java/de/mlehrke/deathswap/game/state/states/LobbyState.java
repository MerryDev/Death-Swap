package de.mlehrke.deathswap.game.state.states;

import de.mlehrke.deathswap.DeathSwapPlugin;
import de.mlehrke.deathswap.game.state.AbstractGameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyState extends AbstractGameState implements Listener {

    private final DeathSwapPlugin plugin;
    private final GameStateContext context;

    public LobbyState(DeathSwapPlugin plugin, GameStateContext context) {
        super(context);
        this.plugin = plugin;
        this.context = context;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void start() {
        World world = Bukkit.getWorlds().getFirst();
        world.getWorldBorder().setCenter(world.getSpawnLocation());
        world.getWorldBorder().setSize(20);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
    }

    @Override
    public void stop() {

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!(this.context.currentState() instanceof LobbyState)) return;

        World world = Bukkit.getWorlds().getFirst();
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(world.getSpawnLocation()), 5L);
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(this.context.currentState() instanceof LobbyState)) return;
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }
}
