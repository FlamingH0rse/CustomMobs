package me.flaming.utils;

import me.flaming.CustomMobsCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class NBTEditor {
    public CustomMobsCore main = CustomMobsCore.getPlugin();
    public void setData (ItemStack item, String key, Object value) {
        if (!item.hasItemMeta() || item.getItemMeta() == null) return;

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
        if (value instanceof String) {
            itemData.set(new NamespacedKey(main, key), PersistentDataType.STRING, (String) value);
        }
        if (value instanceof Integer) {
            itemData.set(new NamespacedKey(main, key), PersistentDataType.INTEGER, (int) value);
        }
        if (value instanceof Double) {
            itemData.set(new NamespacedKey(main, key), PersistentDataType.DOUBLE, (double) value);
        }
    }
    public void rename (ItemStack item, String name) {
        if (!item.hasItemMeta() || item.getItemMeta() == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ColorUtils.getColored(name));
        item.setItemMeta(itemMeta);
    }
}
