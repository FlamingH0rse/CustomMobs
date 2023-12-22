package me.flaming;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class EntitySpawner {
    public boolean isSafeLocation(Location location) {
        World world = location.getWorld();

        // Check if the chunk is loaded
        if (!world.isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return false;
        }

        // Check if the location is safe for a mob to spawn
        return isLocationSafe(location);
    }

    private boolean isLocationSafe(Location location) {
        // Add your custom logic to determine if the location is safe
        // For example, you can check block types, light levels, etc.
        Block block = location.getBlock();

        // Check if the block at the location is air
        if (!block.getType().isAir()) {
            return false;
        }

        // You can add more conditions based on your requirements
        return true;
    }
}
