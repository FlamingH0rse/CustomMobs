package me.flaming;

import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.utils.utils.colorStr;
import static org.bukkit.Bukkit.getWorlds;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EntitySpawnLogic {
    private static final HashMap<String, World> Worlds = new HashMap<>();
    private static final HashMap<String, CustomEntity> LoadedMobs = new HashMap<>();

    public static void StartSpawnLogic() {
        for (World w : getWorlds()) {
            getPlugin().getLogger().info(w.getName());
            GetWorlds().put(w.getName(), w);
        }

        LoadMobs();
    }

    // You can also ignore the value this returns which is fine
    public static String SpawnMob(@NotNull Location location, @NotNull String mobInternalName) {
        CustomEntity mob = GetMobs().get(mobInternalName);

        if (mob == null) {
            return colorStr("&cThe specified mob does not exist in the current context");
        }

        Entity entity = location.getWorld().spawnEntity(location, mob.getType());
        // I don't think this will ever happen as it will be handled internally anyways
        if(!(entity instanceof LivingEntity)) {
            entity.remove();
            return colorStr("&cType needs to be a living entity");
        }
        LivingEntity living = (LivingEntity) entity;
        NamespacedKey key = new NamespacedKey(getPlugin(), "JDnD-weDJ-KDe-DSaw");

        // General Information about the mob
        living.getPersistentDataContainer().set(key, PersistentDataType.STRING, mobInternalName);
        living.setCustomName(mob.getDisplayName());

        // Stats
        living.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mob.getHealth());
        living.setHealth(mob.getHealth());
        living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mob.getDamage());
        living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mob.getSpeed());

        return colorStr("&aSuccessfully spawned the mob!");
    }

    // Not to be confused with getWorlds() from bukkit
    public static HashMap<String, World> GetWorlds() {
        return Worlds;
    }
    public static HashMap<String, CustomEntity> GetMobs() {
        return LoadedMobs;
    }

    private static void LoadMobs() {
        // Loading mobs from yml logic
        // This is where CustomEntity will be made. Default values will also be provided in here in case
        // the user has not specified any values in their respective yml
    }
}
