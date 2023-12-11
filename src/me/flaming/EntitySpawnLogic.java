package me.flaming;

import static me.flaming.CustomMobsCore.getPlugin;
import static org.bukkit.Bukkit.getWorlds;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;

public class EntitySpawnLogic {
    private static HashMap<String, World> Worlds = new HashMap<>();

    public static void StartSpawnLogic() {
        for (World w : getWorlds()) {
            getPlugin().getLogger().info(w.getName());
            GetWorlds().put(w.getName(), w);
        }

        LoadMobs();
    }

    public static void SpawnMob(Location location) {

    }

    // Not to be confused with getWorlds() from bukkit
    public static HashMap<String, World> GetWorlds() {
        return Worlds;
    }

    private static void LoadMobs() {
        // Loading mobs from yml logic
    }
}
