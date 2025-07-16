import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeathSwapPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("DeathSwapPlugin enabled!");
    }

    private List<Material> getAllItems() {
        return Stream.of(Material.values())
                .filter(Material::isItem)
                .collect(Collectors.toList());
    }
}

