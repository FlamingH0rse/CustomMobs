package me.flaming.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.Map;
import java.util.Random;

public class utils {
    public static String arrayOrDefaultValue(String[] args, int index, String defaultValue) {
        if (args.length <= index) return defaultValue;
        return args[index];
    }
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); //match a number with optional '-' and decimal.
    }
    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }
    public static String colorStr(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static <K, V> V getRandomValue(Map<K, V> map) {
        if (!map.isEmpty()) {
            // Obtain a collection of values
            Object[] values = map.values().toArray();

            // Use a random index to get a random value
            Random random = new Random();
            return (V) values[random.nextInt(values.length)];
        } else {
            return null; // Map is empty
        }
    }
}
