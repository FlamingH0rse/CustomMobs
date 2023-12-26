package me.flaming.events;

import me.flaming.classes.CustomEntity;
import me.flaming.classes.CustomMobItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import java.util.Map;
import java.util.Objects;
import static me.flaming.CustomMobsCore.getLoadedMobs;
import static me.flaming.CustomMobsCore.getPlugin;

public class EntityDeathListener implements Listener {
    NamespacedKey key = new NamespacedKey(getPlugin(), "JDnD-weDJ-KDe-DSaw");
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        // Do stuff
        Entity entity = e.getEntity();
        String mobName = entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        for (Map.Entry<String, CustomEntity> mob : getLoadedMobs().entrySet()) {
            if (Objects.equals(mobName, mob.getKey())) {
                e.getDrops().clear();
                e.setDroppedExp(0);

                double randomNumber = 1.0 - Math.random();
                for (Map.Entry<CustomMobItem, Double> itemMap : mob.getValue().getMobDrops().entrySet()) {
                    if (randomNumber < itemMap.getValue()) {
                        getPlugin().getLogger().info("worked iguess");
                        e.getDrops().add(constructItem(itemMap.getKey()));
                    }
                }

                return;
            }
        }
    }

    public ItemStack constructItem(CustomMobItem customMobItem) {
        UnsafeValues unsafe = Bukkit.getUnsafe();

        Material material = customMobItem.getItem().getKey();
        String itemMetaString = customMobItem.getItem().getValue();

        ItemStack item = new ItemStack(material);
        item = unsafe.modifyItemStack(item, itemMetaString);

        return item;
    }
}
