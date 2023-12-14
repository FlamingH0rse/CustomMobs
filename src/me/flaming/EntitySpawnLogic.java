package me.flaming;

import static me.flaming.CustomMobsCore.getPlugin;
import static org.bukkit.Bukkit.getWorlds;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class EntitySpawnLogic {
    private static final HashMap<String, World> Worlds = new HashMap<>();
    private static final HashMap<String, CustomEntity> LoadedMobs = new HashMap<>();
    private static final FileConfiguration pluginConfig = getPlugin().getConfig();

    public static void StartSpawnLogic() {
        for (World w : getWorlds()) {
            getPlugin().getLogger().info(w.getName());
            GetWorlds().put(w.getName(), w);
        }

        LoadMobs();
    }

    // Not to be confused with getWorlds() from bukkit
    public static HashMap<String, World> GetWorlds() {
        return Worlds;
    }
    public static HashMap<String, CustomEntity> GetMobs() {
        return LoadedMobs;
    }
    public static FileConfiguration GetPluginConfig() { return pluginConfig; }

    private static void LoadMobs() {
        // Loading mobs from yml logic
        // This is where CustomEntity will be made. Default values will also be provided in here in case
        // the user has not specified any values in their respective yml
        FileConfiguration cfg = GetPluginConfig();

        for (String mob : cfg.getConfigurationSection("mobs").getKeys(false)) {
            EntityType mobType = EntityType.ZOMBIE;

            double mobHealth = cfg.getDouble(mob + ".stats.health");
            double mobDamage = cfg.getDouble(mob + ".stats.health");
            double mobSpeed = cfg.getDouble(mob + ".stats.health");

            CustomEntity mobClass = CustomEntity.EntityBuilder
                    .newEntity()
                    .setDisplayName(cfg.getString(mob + ".displayName"))
                    .setEntityType(mobType)
                    .setHealth(mobHealth)
                    .setDamage(mobDamage)
                    .setSpeed(mobSpeed)
                    .build();

            getPlugin().getLogger().info(mob);
        }
    }

    private static boolean Verifier() {
        return false;
    }
}
