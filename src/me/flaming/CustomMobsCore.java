package me.flaming;

import me.flaming.classes.CustomEntity;
import me.flaming.commands.PluginCommands;
import me.flaming.events.EntityDeathListener;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import static me.flaming.EntityLoader.startSpawnLogic;

public class CustomMobsCore extends JavaPlugin {
    private static CustomMobsCore plugin;
    private static final HashMap<String, World> Worlds = new HashMap<>();
    private static final HashMap<String, CustomEntity> LoadedMobs = new HashMap<>();
    private static final FileConfiguration pluginConfig = getPlugin().getConfig();

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        startSpawnLogic();
        getCommand("custommobs").setExecutor(new PluginCommands());
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);

        getLogger().info("CustomMobs is Enabled");
    }

    @Override
    public void onDisable() {
        // Cleanup logic here
        getLogger().info("CustomMobs Disabled");
    }
    public static CustomMobsCore getPlugin() {
        return plugin;
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
}
