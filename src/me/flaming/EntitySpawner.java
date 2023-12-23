package me.flaming;

import me.flaming.classes.CustomEntity;
import me.flaming.classes.SpawnLocation;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.EntityLoader.getLoadedMobs;

public class EntitySpawner extends BukkitRunnable {
    @Override
    public void run() {
        int times = 0;
        for (CustomEntity mob : getLoadedMobs().values()) {
            if (mob.getSpawnLocation().getProperty().isEnabled()) {
                times++;
                Location spawnLocation = spawnProcess(mob, 10);
                if (spawnLocation != null) {
                    EntityUtils entityUtils = new EntityUtils();
                    entityUtils.spawnMob(spawnLocation, mob.getInternalName());
                }
            }
        }
        // Atleast one mob spawn is enabled
        if (times > 0) {
            this.runTaskLater(getPlugin(), 20L);
        }
    }

    @Nullable
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
        return null;
    }
}
