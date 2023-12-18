package me.flaming.classes;

import org.bukkit.Material;
import java.util.AbstractMap;
import java.util.Map;

public class CustomMobItem {
    private final Map.Entry<Material, String> item;

    public CustomMobItem(Material key, String value) {
        this.item = new AbstractMap.SimpleEntry<>(key, value);
    }

    public CustomMobItem(Map<String, Object> input) {
        if (input == null || input.isEmpty()) {
            this.item = null;
            return;
        }

        Map.Entry<String, Object> entry = input.entrySet().iterator().next();

        Material material = Material.getMaterial(entry.getKey().toUpperCase());
        String meta = entry.getValue().toString();

        this.item = new AbstractMap.SimpleEntry<>(material, meta);
    }

    public Map.Entry<Material, String> getItem() {
        return item;
    }
}