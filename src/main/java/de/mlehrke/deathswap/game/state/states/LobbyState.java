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
        World world = Bukkit.getWorld("pregame");
        if (world == null) {
            plugin.getLogger().severe("Pregame World existiert nicht! Start abgebrochen.");
            return;
        }
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setClearWeatherDuration(Integer.MAX_VALUE);
        preparePlayers();
    }

    @Override
    public void stop() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!(this.context.currentState() instanceof LobbyState)) return;

        World world = Bukkit.getWorld("pregame");
        if (world == null) return;
        Player player = event.getPlayer();
        player.heal(20);
        player.setFoodLevel(20);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(world.getSpawnLocation()), 5L);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(this.context.currentState() instanceof LobbyState)) return;
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }

    private void preparePlayers() {
        World world = Bukkit.getWorld("pregame");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.heal(20);
            player.setFoodLevel(20);
            player.teleport(world.getSpawnLocation());
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.setInvulnerable(true);
        }
    }
}
