package me.flaming;

import me.flaming.classes.CustomEntity;
import me.flaming.classes.SpawnLocation;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import static me.flaming.CustomMobsCore.getLoadedWorlds;
import static me.flaming.CustomMobsCore.getPlugin;

public class EntitySpawnerTask extends BukkitRunnable {
    private final CustomEntity mob;
    public EntitySpawnerTask(@NotNull CustomEntity input) {
        this.mob = input;
    }

    @Override
    public void run() {
        int maxIteration = getRandomMobAmount(mob);

        for (int i = 0; i < maxIteration; i++) {
            if (mobAmountIsMax(mob)) {
                break;
            }

            Location spawnLocation = spawnProcess(mob, 10);
            if (spawnLocation != null) {
                EntityUtils entityUtils = new EntityUtils();
                entityUtils.spawnMob(spawnLocation, mob.getInternalName());
                // Prolly add info here that says that the mob spawned or something
            }
        }

        // Schedule it again
        runAgain();
    }

    @Nullable
    // Ignore the field maxTries is always 10 warning. It may be dynamic later and needed after some updates
    private Location spawnProcess(CustomEntity mob, int maxTries) {
        EntityUtils entityUtils = new EntityUtils();
        EntitySpawnerUtils spawnerUtils = new EntitySpawnerUtils();
        SpawnLocation spawnLocation = mob.getSpawnLocation();

        Vector higher = spawnerUtils.getHigherValueVector(spawnLocation.getPos1(), spawnLocation.getPos2());
        Vector lower = spawnerUtils.getLowerValueVector(spawnLocation.getPos1(), spawnLocation.getPos2());
        for (int attempt = 0; attempt < maxTries; attempt++) {
            Vector rnd = spawnerUtils.getRandomVector(higher, lower);
            Location locationWithPotentialSurface = spawnerUtils.getPotentialSurface(rnd, mob);

            if (locationWithPotentialSurface != null) {
                Entity entity = entityUtils.getEntityReference(mob.getType());
                if (entity != null && spawnerUtils.isSafeLocation(locationWithPotentialSurface, entity)) {
                    return locationWithPotentialSurface;
                }
            }
        }

        // Fail
        return null;
    }

    private void runAgain() {
        EntitySpawnerUtils spawnerUtils = new EntitySpawnerUtils();

        long randomInterval = spawnerUtils.getRandomInterval(mob);
        EntitySpawnerTask mobSpawnerTask = new EntitySpawnerTask(mob);
        mobSpawnerTask.runTaskLater(getPlugin(), randomInterval);
        this.cancel();
    }

    private int getRandomMobAmount(@NotNull CustomEntity mob) {
        int minAmount = mob.getSpawnLocation().getProperty().getMinAmount();
        int maxAmount = mob.getSpawnLocation().getProperty().getMaxAmount();

        if (minAmount > maxAmount) {
            return 1;
        }

        EntitySpawnerUtils spawnerUtils = new EntitySpawnerUtils();
        return spawnerUtils.randomizer(maxAmount, minAmount);
    }

    private boolean mobAmountIsMax(@NotNull CustomEntity mob) {
        int maxAmount = mob.getSpawnLocation().getProperty().getMaxMob();
        World currentWorld = getLoadedWorlds().get(mob.getSpawnLocation().getWorldName());
        return entityCounter(mob, currentWorld) >= maxAmount;
    }

    private int entityCounter(@NotNull CustomEntity mob, @NotNull World currentWorld) {
        int amount = 0;
        NamespacedKey key = new NamespacedKey(getPlugin(), "JDnD-weDJ-KDe-DSaw");

        for (LivingEntity mobInstance : currentWorld.getLivingEntities()) {
            String mobName = mobInstance.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (Objects.equals(mobName, mob.getInternalName())) {
                amount++;
            }
        }

        return amount;
    }
}
