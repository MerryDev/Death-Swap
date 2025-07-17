package de.mlehrke.deathswap.game.state.states;

import de.mlehrke.deathswap.DeathSwapPlugin;
import de.mlehrke.deathswap.game.state.AbstractGameState;
import de.mlehrke.deathswap.game.state.GameStateContext;
import de.mlehrke.deathswap.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InGameState extends AbstractGameState implements Listener {

    private final DeathSwapPlugin plugin;
    private final GameStateContext context;
    private final List<Material> allItems;
    private WorldUtil worldUtil;

    private final Set<Material> STACKING_PLANTS = Set.of(
            Material.SUGAR_CANE,
            Material.CACTUS,
            Material.BAMBOO,
            Material.KELP,
            Material.KELP_PLANT,
            Material.TWISTING_VINES,
            Material.TWISTING_VINES_PLANT,
            Material.WEEPING_VINES,
            Material.WEEPING_VINES_PLANT
    );

    public InGameState(DeathSwapPlugin plugin, GameStateContext context) {
        super(context);
        worldUtil = new WorldUtil(plugin, new Random());
        this.plugin = plugin;
        this.context = context;
        this.allItems = getAllItems();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void start() {
        worldUtil.createWorldAsync("game", World.Environment.NORMAL)
                .thenAccept(world -> {
                    // Jetzt ist die Welt sicher geladen
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.teleport(world.getSpawnLocation());
                            player.setGameMode(GameMode.SURVIVAL);
                            player.setInvulnerable(false);
                            ItemStack beef = new ItemStack(Material.COOKED_BEEF);
                            beef.setAmount(64);
                            player.getInventory().addItem(beef);
                        }

                        plugin.swapper().start();
                        context.timer().paused(false);
                        context.timer().start();
                    });
                })
                .exceptionally(ex -> {
                    plugin.getLogger().severe("Fehler beim Laden der Welt: " + ex.getMessage());
                    return null;
                });
    }
    @Override
    public void stop() {
        context.timer().reset();
        plugin.swapper().stop();

        List<World> worlds =List.of(
                Objects.requireNonNull(Bukkit.getWorld("game")),
                Objects.requireNonNull(Bukkit.getWorld("game_nether")),
                Objects.requireNonNull(Bukkit.getWorld("game_the_end"))
        );
        for (World world : worlds) {
            if (world == null) continue;
            worldUtil.deleteWorld(world);
        }
    }

    // Event logic

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!(this.context.currentState() instanceof InGameState)) return;
        Player player = event.getPlayer();
        World world = event.getPlayer().getWorld();
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(world.getSpawnLocation()), 5L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!(this.context.currentState() instanceof InGameState)) return;
        Player player = event.getPlayer();
        World world = Bukkit.getWorld("game");
        if(world == null) return;
        player.setGameMode(GameMode.SPECTATOR);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(world.getSpawnLocation()), 5L);

    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(this.context.currentState() instanceof InGameState)) return;
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        event.setDropItems(false);

        List<ItemStack> randomizedDrops = getRandomizedDrops(block);
        randomizedDrops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation(), drop));

        // Chain-Reaction für gestapelte Pflanzen
        if (STACKING_PLANTS.contains(block.getType())) {
            Block current = block.getRelative(BlockFace.UP);
            while (STACKING_PLANTS.contains(current.getType())) {
                List<ItemStack> drops = getRandomizedDrops(current);
                Block finalCurrent = current;
                drops.forEach(drop -> finalCurrent.getWorld().dropItemNaturally(finalCurrent.getLocation(), drop));
                current.setType(Material.AIR); // Manuell abbauen, sonst droppt Minecraft selbst
                current = current.getRelative(BlockFace.UP);
            }
        }
    }

    private List<ItemStack> getRandomizedDrops(Block block) {
        Random random = ThreadLocalRandom.current();

        Collection<ItemStack> originalDrops = block.getDrops();
        List<ItemStack> randomized = new ArrayList<>();

        for (ItemStack original : originalDrops) {
            int amount = original.getAmount();
            if (amount <= 0) continue;
            Material randomMaterial = allItems.get(random.nextInt(allItems.size()));
            randomized.add(new ItemStack(randomMaterial, original.getAmount()));
        }
        return randomized;
    }

    private List<Material> getAllItems() {
        return Stream.of(Material.values())
                .filter(Material::isItem)
                .filter(this::isNotArmor)
                .filter(material -> material != Material.TOTEM_OF_UNDYING)
                .filter(material -> material != Material.KNOWLEDGE_BOOK)
                .filter(material -> material != Material.WRITTEN_BOOK)
                .filter(material -> material != Material.ENCHANTED_BOOK)
                .filter(material -> material != Material.GOAT_HORN)
                .filter(material -> material != Material.JIGSAW)
                .filter(material -> material != Material.DEBUG_STICK)
                .filter(material -> material != Material.LIGHT)
                .filter(material -> !material.name().contains("POTTERY_SHERD"))
                .filter(material -> !material.name().contains("DISC"))
                .filter(material -> !material.name().contains("LEGACY"))
                .filter(material -> !material.name().contains("COMMAND"))
                .filter(material -> !material.name().contains("STRUCTURE"))
                .filter(material -> !material.name().contains("BED"))
                .filter(material -> !material.name().contains("POTION"))
                .filter(material -> !material.name().contains("TIPPED_ARROW"))
                .filter(material -> !material.name().contains("TEMPLATE"))
                .filter(material -> !material.name().contains("PATTERN"))
                .collect(Collectors.toList());

    }

    private boolean isNotArmor(Material material) {
        return !(material.name().contains("CHESTPLATE") ||
                material.name().contains("LEGGINGS") ||
                material.name().contains("BOOTS") ||
                material.name().contains("HELMET")
        );
    }
}
