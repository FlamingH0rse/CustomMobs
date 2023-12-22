package me.flaming;

import me.flaming.classes.CustomEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import java.util.Random;

public class EntitySpawnerUtils {
    public boolean isSafeLocation(Location location, Entity entity) {
        World world = location.getWorld();

        // Check if the chunk is loaded (idk if this is needed)
        /*if (!world.isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return false;
        }*/

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

    public void withPotentialSurface(Location randomLocation, CustomEntity currentMob) {

    }

    public Vector getRandomLocation(Vector pos1, Vector pos2) {
        double x = randomizer(pos1.getX(), pos2.getX());
        double y = randomizer(pos1.getY(), pos2.getY());
        double z = randomizer(pos1.getZ(), pos2.getZ());

        return new Vector(x, y, z);
    }

    private double randomizer(double pos1Component, double pos2Component) {
        Random random = new Random();
        return pos1Component - random.nextInt((int) (pos1Component - pos2Component));
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
