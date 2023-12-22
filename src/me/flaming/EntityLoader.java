package me.flaming;

import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.utils.utils.getRandomValue;
import static org.bukkit.Bukkit.getWorlds;
import me.flaming.classes.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;

public class EntityLoader {
    private static final HashMap<String, World> Worlds = new HashMap<>();
    private static final HashMap<String, CustomEntity> LoadedMobs = new HashMap<>();
    private static final FileConfiguration pluginConfig = getPlugin().getConfig();

    public static void startSpawnLogic() {
        for (World w : getWorlds()) {
            getPlugin().getLogger().info(w.getName());
            getLoadedWorlds().put(w.getName(), w);
        }

        loadMobs();
    }

    public static HashMap<String, World> getLoadedWorlds() {
        return Worlds;
    }
    public static HashMap<String, CustomEntity> getLoadedMobs() {
        return LoadedMobs;
    }
    public static FileConfiguration getPluginConfig() {
        return pluginConfig;
    }

    private static void loadMobs() {
        // Loading mobs from yml logic
        // This is where CustomEntity will be made. Default values will also be provided in here in case
        // the user has not specified any values in their respective yml
        World randomWorld = getRandomValue(getLoadedWorlds());
        FileConfiguration cfg = getPluginConfig();
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

            // Load mob inventory
            ConfigurationSection inventorySection = cfg.getConfigurationSection(configPath + ".inventory");
            EntityInventory entityInv = loadMobInventory(inventorySection);

            // Load mob drops
            ConfigurationSection dropsSection = cfg.getConfigurationSection(configPath + ".properties.drops");
            HashMap<CustomMobItem , Double> mobdrops = loadMobDrops(dropsSection);

            // Load spawn location
            ConfigurationSection spawnSection = cfg.getConfigurationSection(configPath + ".properties.spawn");
            SpawnLocation spawnLocation = loadMobSpawnLocation(spawnSection);

            // Build the Entity
            CustomEntity mobClass = CustomEntity.EntityBuilder
                    .newEntity()
                    .setDisplayName(cfg.getString(configPath + ".display_name"))
                    .setEntityType(mobType)
                    .setHealth(mobHealth)
                    .setDamage(mobDamage)
                    .setSpeed(mobSpeed)
                    .setInventory(entityInv)
                    .setMobDrops(mobdrops)
                    .setSpawnLocation(spawnLocation)
                    .build();

            getLoadedMobs().put(mob, mobClass);
            getPlugin().getLogger().info("Successfully loaded mob: " + mob);
        }
    }

    private static void verifyIntegrity() {

    }

    private static void setMobsStats() {

    }

    private static EntityInventory loadMobInventory(@Nullable ConfigurationSection inventorySection) {
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

        return entityInv;
    }

    private static HashMap<CustomMobItem , Double> loadMobDrops(@Nullable ConfigurationSection dropsSection) {
        HashMap<CustomMobItem , Double> mobdrops = new HashMap<>();
        if (dropsSection != null) {
            for (String itemName : dropsSection.getKeys(false)) {
                ConfigurationSection drop = dropsSection.getConfigurationSection(itemName);
                Material material = Material.getMaterial(itemName);
                if (drop == null || material == null) {
                    continue;
                }

                String metaString = drop.getString("meta");
                double chance = drop.getDouble("chance", 1.0);
                CustomMobItem item = new CustomMobItem(material, metaString);
                mobdrops.put(item, chance);
            }
        }

        return mobdrops;
    }

    private static SpawnLocation loadMobSpawnLocation(@Nullable ConfigurationSection spawnSection) {
        SpawnLocation spawnLocation = null;
        if (spawnSection != null) {
            long minInterval = spawnSection.getLong("tick-interval.min", -1);
            long maxInterval = spawnSection.getLong("tick-interval.max", minInterval);
            int minAmount = spawnSection.getInt("spawn-amount-range.min", -1);
            int maxAmount = spawnSection.getInt("spawn-amount-range.max", minAmount);
            int maxMob = spawnSection.getInt("max-mob");
            boolean enabled = spawnSection.getBoolean("enabled", false);

            ConfigurationSection spawnLocationSection = spawnSection.getConfigurationSection("location");
            Vector pos1 = new Vector();
            Vector pos2 = new Vector();

            if (spawnLocationSection != null) {
                pos1.setX(spawnLocationSection.getDouble("pos1.x"));
                pos1.setY(spawnLocationSection.getDouble("pos1.y"));
                pos1.setZ(spawnLocationSection.getDouble("pos1.z"));
                // Probably not needed ig
                /*if (pos1.getX() == 0 && pos1.getY() == 0 && pos1.getZ() == 0) {

                }*/
                pos2.setX(spawnLocationSection.getDouble("pos2.x"));
                pos2.setY(spawnLocationSection.getDouble("pos2.y"));
                pos2.setZ(spawnLocationSection.getDouble("pos2.z"));
            } else {
                enabled = false;
                getPlugin().getLogger().warning("No spawn location supplied. Setting enabled to false");
            }

            SpawnProperty spawnProperty = new SpawnProperty(minInterval, maxInterval, minAmount, maxAmount, maxMob, enabled);
            spawnLocation = new SpawnLocation(pos1, pos2, spawnProperty);
        }

        return spawnLocation;
    }
}
