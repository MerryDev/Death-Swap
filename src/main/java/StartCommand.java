import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final DeathSwapPlugin swapper;

    public StartCommand(DeathSwapPlugin swapper) {
        this.swapper = swapper;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new PlayerSwapper(swapper).start();
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
