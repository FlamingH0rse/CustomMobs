package me.flaming.events;

import me.flaming.classes.CustomEntity;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import java.util.Map;
import java.util.Objects;
import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.EntitySpawnLogic.GetMobs;

public class EntityDeathListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        NamespacedKey key = new NamespacedKey(getPlugin(), "JDnD-weDJ-KDe-DSaw");
        // Do stuff
        Entity entity = e.getEntity();
        String mobName = entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        for (Map.Entry<String, CustomEntity> mobs : GetMobs().entrySet()) {
            if (Objects.equals(mobName, mobs.getKey())) {
                e.getDrops().clear();
                e.setDroppedExp(0);
                return;
            }
        }
    }
}
