package me.flaming.utils;

import me.flaming.CustomMobsCore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    public CustomMobsCore main = CustomMobsCore.getPlugin();
    public static String getColored(String str) {
        String coloredStr = ChatColor.translateAlternateColorCodes('&', str);

        Pattern hexRegex = Pattern.compile("<##[a-zA-Z0-9]*>");
        Matcher matches = hexRegex.matcher(coloredStr);

        while (matches.find()) {
            String hexCode = "#" + matches.group().replaceAll("<##", "").replaceAll(">", "");
            coloredStr = coloredStr.replaceAll(matches.group(), net.md_5.bungee.api.ChatColor.of(hexCode) + "");
        }
        return coloredStr;
    }
    public void send (Player player, String message) {
        String coloredStr = ChatColor.translateAlternateColorCodes('&', message);

        Pattern hexRegex = Pattern.compile("<##[a-zA-Z0-9]*>");
        Matcher matches = hexRegex.matcher(coloredStr);

        while (matches.find()) {
            String hexCode = "#" + matches.group().replaceAll("<##", "").replaceAll(">", "");
            coloredStr = coloredStr.replaceAll(matches.group(), net.md_5.bungee.api.ChatColor.of(hexCode) + "");
        }
        player.sendMessage(coloredStr);
    }
    public void send (Player player, String message, ChatMessageType type) {
        String coloredStr = ChatColor.translateAlternateColorCodes('&', message);

        Pattern hexRegex = Pattern.compile("<##[a-zA-Z0-9]*>");
        Matcher matches = hexRegex.matcher(coloredStr);

        while (matches.find()) {
            String hexCode = "#" + matches.group().replaceAll("<##", "").replaceAll(">", "");
            coloredStr = coloredStr.replaceAll(matches.group(), net.md_5.bungee.api.ChatColor.of(hexCode) + "");
        }
        player.spigot().sendMessage(type, TextComponent.fromLegacyText(coloredStr));
    }
    public void sendLines (Player player, List<String> messages) {
        for (String message : messages) {
            send(player, message);
        }
    }
    public void sendLines (Player player, List<String> messages, ChatMessageType type) {
        for (String message : messages) {
            send(player, message, type);
        }
    }
    public void log (String message) {
        main.getLogger().info("");
    }
    public void logLines (String message) {

    }
}
