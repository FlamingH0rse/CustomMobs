package me.flaming;

import static me.flaming.CustomMobsCore.getPlugin;
import static org.bukkit.Bukkit.getWorlds;
import org.bukkit.World;

import java.util.HashMap;

public class EntitySpawnLogic {
    public static void StartSpawnLogic() {
        HashMap<String, World> worlds = new HashMap<>();

        for (World w : getWorlds()) {
            getPlugin().getLogger().info(w.getName());
            worlds.put(w.getName(), w);
        }

        LoadMobs();
    }

    static void LoadMobs() {

    }
}
