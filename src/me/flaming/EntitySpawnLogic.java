package me.flaming;

import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.utils.utils.getRandomValue;
import static org.bukkit.Bukkit.getWorlds;
import me.flaming.classes.CustomEntity;
import me.flaming.classes.CustomMobItem;
import me.flaming.classes.EntityInventory;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

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
        World randomWorld = getRandomValue(GetWorlds());
        FileConfiguration cfg = GetPluginConfig();
        getPlugin().getLogger().info("Started mob loading");

        for (String mob : cfg.getConfigurationSection("mobs").getKeys(false)) {
            String configPath = "mobs." + mob;

            EntityType mobType;
            try {
                mobType = EntityType.valueOf(cfg.getString(configPath + ".type", "none").toUpperCase());
            } catch (IllegalArgumentException e) {
                getPlugin().getLogger().warning("Failed to load mob: " + mob + " Invalid Type specified");
                continue;
            }

            Location location = new Location(randomWorld, 0, 0, 0);
            LivingEntity entityInstance = (LivingEntity) randomWorld.createEntity(location, mobType.getEntityClass());

            // Entity is not a mob
            if (!(entityInstance instanceof Mob)) {
                getPlugin().getLogger().warning("Failed to load mob: " + mob + " EntityType is not a mob");
                continue;
            }

//            Class<LivingEntity> livingEntityClass = (Class<LivingEntity>) mobType.getEntityClass();

            double mobHealth = cfg.getDouble(configPath + ".stats.health");
            double mobDamage = cfg.getDouble(configPath + ".stats.health");
            double mobSpeed = cfg.getDouble(configPath + ".stats.health");

            ConfigurationSection inventorySection = cfg.getConfigurationSection(configPath + "inventory");
            EntityInventory entityInv = null;

            if (inventorySection != null) {
                entityInv = EntityInventory.InventoryBuilder.newInventory()
                        .setMainHand(new CustomMobItem(inventorySection.getConfigurationSection("main-hand").getValues(false)))
                        .setLeftHand(new CustomMobItem(inventorySection.getConfigurationSection("left-hand").getValues(false)))
                        .setHead(new CustomMobItem(inventorySection.getConfigurationSection("head").getValues(false)))
                        .setBody(new CustomMobItem(inventorySection.getConfigurationSection("body").getValues(false)))
                        .setLegs(new CustomMobItem(inventorySection.getConfigurationSection("legs").getValues(false)))
                        .setFoot(new CustomMobItem(inventorySection.getConfigurationSection("foot").getValues(false)))
                        .build();
            }

            CustomEntity mobClass = CustomEntity.EntityBuilder
                    .newEntity()
                    .setDisplayName(cfg.getString(configPath + ".displayName"))
                    .setEntityType(mobType)
                    .setHealth(mobHealth)
                    .setDamage(mobDamage)
                    .setSpeed(mobSpeed)
                    .setInventory(entityInv)
                    .build();

            GetMobs().put(mob, mobClass);
            getPlugin().getLogger().info("Successfully loaded mob: " + mob);
        }
    }

    private static boolean Verifier() {
        return false;
    }
}
