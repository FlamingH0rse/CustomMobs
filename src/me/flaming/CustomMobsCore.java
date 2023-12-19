package me.flaming;

import me.flaming.commands.PluginCommands;
import me.flaming.events.EntityDeathListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import static me.flaming.EntitySpawnLogic.StartSpawnLogic;

public class CustomMobsCore extends JavaPlugin {
    private static CustomMobsCore plugin;
    private File dataConfigFile;
    private FileConfiguration dataConfig;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        createDataConfig();
        StartSpawnLogic();
        getCommand("custommobs").setExecutor(new PluginCommands());
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);

        getLogger().info("CustomMobs is Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomMobs Disabled");
    }

    public static CustomMobsCore getPlugin() {
        return plugin;
    }

    public File getDataFile() {
        return dataConfigFile;
    }

    public FileConfiguration getDataCfg() {
        return dataConfig;
    }

    public static void saveDataCfg() {
        try {
            plugin.getDataCfg().save(plugin.getDataFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDataConfig() {
        dataConfigFile = new File(getDataFolder(), "data.yml");
        if (!dataConfigFile.exists()) {
            dataConfigFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataConfigFile);
    }
}
