package me.flaming;

import me.flaming.classes.CustomEntity;
import me.flaming.classes.SpawnLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Random;
import static me.flaming.CustomMobsCore.*;

public class EntitySpawnerUtils {
    public static void startSpawnerLogic() {
        EntitySpawnerUtils spawnerUtils = new EntitySpawnerUtils();
        int amount = 0;
        for (CustomEntity mob : getLoadedMobs().values()) {
            if (mob.getSpawnLocation().getProperty().isEnabled()) {
                long randomInterval = spawnerUtils.getRandomInterval(mob);
                EntitySpawnerTask mobSpawnerTask = new EntitySpawnerTask(mob);
                mobSpawnerTask.runTaskLater(getPlugin(), randomInterval);
                amount++;
            }
        }

        getPlugin().getLogger().info("Started " + amount + " spawner/s");
    }

    public boolean isSafeLocation(Location location, Entity entity) {
        World world = location.getWorld();

        BoundingBox box = entity.getBoundingBox();

        // Mob size
        int sizeX = (int) Math.round(box.getWidthX());
        int sizeY = (int) Math.round(box.getHeight());
        int sizeZ = (int) Math.round(box.getWidthZ());
        int startX = (sizeX - 1)/2;
        int startZ = (sizeZ - 1)/2;

        double componentX = location.getX() - startX;
        double componentY = location.getY();
        double componentZ = location.getZ() - startZ;

        for (int x = 0; x < sizeX ; x++) {
            for (int y = 0; y < sizeY ; y++) {
                for (int z = 0; z < sizeZ ; z++) {
                    double localComponentX = componentX + x;
                    double localComponentY = componentY + y;
                    double localComponentZ = componentZ + z;

                    Location referenceLocation = new Location(world, localComponentX, localComponentY, localComponentZ);
                    if (!isLocationSafe(referenceLocation)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    public Location getPotentialSurface(Vector randomVector, CustomEntity currentMob) {
        SpawnLocation mobLocationProperty = currentMob.getSpawnLocation();
        int height = 1 + (int) Math.round(Math.abs(mobLocationProperty.getPos1().getY() - mobLocationProperty.getPos2().getY()));

        for (int i = 0; i < height; i++) {
            Location surface = tryAndGetSurface(randomVector, mobLocationProperty.getWorldName(), i, false);
            if (surface != null) {
                return surface;
            }
        }

        for (int i = 0; i < height; i++) {
            Location surface = tryAndGetSurface(randomVector, mobLocationProperty.getWorldName(), i, true);
            if (surface != null) {
                return surface;
            }
        }

        return null;
    }

    public long getRandomInterval(@NotNull CustomEntity mob) {
        long interval1 = mob.getSpawnLocation().getProperty().getMinInterval();
        if (interval1 < 0) {
            return 1;
        }
        long interval2 = mob.getSpawnLocation().getProperty().getMaxInterval();
        long higherInterval = Math.max(interval1, interval2);
        long lowerInterval = Math.min(interval1, interval2);

        return randomizer(higherInterval, lowerInterval);
    }

    @NotNull
    public Vector getRandomVector(Vector pos1, Vector pos2) {
        double x = randomizer(pos1.getX(), pos2.getX());
        double y = randomizer(pos1.getY(), pos2.getY());
        double z = randomizer(pos1.getZ(), pos2.getZ());

        return new Vector(x, y, z);
    }

    @NotNull
    public Vector getHigherValueVector(@NotNull Vector pos1, @NotNull Vector pos2) {
        double x = Math.max(pos1.getX(), pos2.getX());
        double y = Math.max(pos1.getY(), pos2.getY());
        double z = Math.max(pos1.getZ(), pos2.getZ());

        return new Vector(x, y, z);
    }

    @NotNull
    public Vector getLowerValueVector(@NotNull Vector pos1, @NotNull Vector pos2) {
        double x = Math.min(pos1.getX(), pos2.getX());
        double y = Math.min(pos1.getY(), pos2.getY());
        double z = Math.min(pos1.getZ(), pos2.getZ());

        return new Vector(x, y, z);
    }

    @Nullable
    private Location tryAndGetSurface(Vector randomVector, String worldName, int num, boolean reverse) {
        double y = (reverse) ? randomVector.getY() - num : randomVector.getY() + num;
        World world = getLoadedWorlds().get(worldName);

        Location location = new Location(world, randomVector.getX(), y, randomVector.getZ());
        Block block = location.clone().subtract(0, 1, 0).getBlock();

        if (location.getBlock().getType().isAir() && block.getType().isSolid()) {
            return location;
        }

        return null;
    }

    private double randomizer(double pos1Component, double pos2Component) {
        double random = Math.random();
        return pos1Component - random * (pos1Component - pos2Component);
    }

    private long randomizer(long a, long b) {
        Random random = new Random();
        return a - random.nextLong((a - b));
    }

    private boolean isLocationSafe(Location location) {
        Block block = location.getBlock();
        return block.getType().isAir();
    }
}
