package me.flaming;

import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.utils.utils.getRandomValue;
import static org.bukkit.Bukkit.getWorlds;
import me.flaming.classes.CustomEntity;
import me.flaming.classes.CustomMobItem;
import me.flaming.classes.EntityInventory;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
            // It seems like paper has an issue here (This will work in paper 1.20.4)
            LivingEntity entityInstance = (LivingEntity) randomWorld.createEntity(location, mobType.getEntityClass());

            // Entity is not a mob
            if (!(entityInstance instanceof Mob)) {
                getPlugin().getLogger().warning("Failed to load mob: " + mob + " EntityType is not a mob");
                continue;
            }

            AttributeInstance defHealth = entityInstance.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            AttributeInstance defDmg = entityInstance.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            AttributeInstance defSpeed = entityInstance.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

            double mobHealth = cfg.getDouble(configPath + ".stats.health", defHealth.getBaseValue());
            double mobDamage = cfg.getDouble(configPath + ".stats.damage", defDmg.getBaseValue());
            double mobSpeed = cfg.getDouble(configPath + ".stats.speed", defSpeed.getBaseValue());

            ConfigurationSection inventorySection = cfg.getConfigurationSection(configPath + ".inventory");
            EntityInventory entityInv = null;

            if (inventorySection != null) {
                ConfigurationSection main_hand = inventorySection.getConfigurationSection("main-hand");
                ConfigurationSection left_hand = inventorySection.getConfigurationSection("left-hand");
                ConfigurationSection head = inventorySection.getConfigurationSection("head");
                ConfigurationSection body = inventorySection.getConfigurationSection("body");
                ConfigurationSection legs = inventorySection.getConfigurationSection("legs");
                ConfigurationSection foot = inventorySection.getConfigurationSection("foot");

                entityInv = EntityInventory.InventoryBuilder.newInventory()
                        .setMainHand(new CustomMobItem((main_hand != null) ? main_hand.getValues(false) : null))
                        .setLeftHand(new CustomMobItem((left_hand != null) ? left_hand.getValues(false) : null))
                        .setHead(new CustomMobItem((head != null) ? head.getValues(false) : null))
                        .setBody(new CustomMobItem((body != null) ? body.getValues(false) : null))
                        .setLegs(new CustomMobItem((legs != null) ? legs.getValues(false) : null))
                        .setFoot(new CustomMobItem((foot != null) ? foot.getValues(false) : null))
                        .build();
            }

            CustomEntity mobClass = CustomEntity.EntityBuilder
                    .newEntity()
                    .setDisplayName(cfg.getString(configPath + ".display_name"))
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
