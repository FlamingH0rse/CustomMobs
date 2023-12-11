package me.flaming.utils;

import net.md_5.bungee.api.ChatColor;

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
}
