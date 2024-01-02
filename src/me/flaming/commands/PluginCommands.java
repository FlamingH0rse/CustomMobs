package me.flaming.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import me.flaming.EntityUtils;

import static me.flaming.CustomMobsCore.getLoadedMobs;
import static me.flaming.utils.utils.arrayOrDefaultValue;
import static me.flaming.utils.utils.colorStr;

public class PluginCommands implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String currentArg = arrayOrDefaultValue(args, 0, "");

        switch (currentArg.toLowerCase()) {
            case "help":
                helpCommand(sender);
                break;
            case "spawnmob":
                spawnMobCommand(sender, args);
                break;
            case "getiteminfo":
                getItemInfoCommand(sender);
                break;
            default:
                aboutMessageCommand(sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> arguments = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("custommobs")) {
            if (args.length == 1) {
                arguments.add("help");
                arguments.add("about");
                arguments.add("spawnmob");
                arguments.add("getiteminfo");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("spawnmob")) {
                arguments.addAll(getLoadedMobs().keySet());
            }
        }
        return arguments;
    }

    // Commands are stored Below
    private void spawnMobCommand(CommandSender sender, String[] args) {
        // Mob Spawn Logic here
        // Basically this will call spawnmob inside entityspawnlogic

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(colorStr("This command is not available in console"));
            return;
        }
        EntityUtils entityUtils = new EntityUtils();
        Player p = (Player) sender;

        // Run the stuff then get the message whether it be a success message or an error one
        String mobInternalName = arrayOrDefaultValue(args, 1, "");
        String message = entityUtils.spawnMob(p.getLocation(), mobInternalName);
        p.sendMessage(message);
    }

    private void getItemInfoCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(colorStr("This command is not available in console"));
            return;
        }
        Player p = (Player) sender;

        ItemStack item = p.getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
        Material material = item.getType();

        if (itemMeta == null) {
            p.sendMessage(colorStr("&cInvalid Item/You are currently not holding any items"));
            return;
        }

        BaseComponent[] metaCopyButton = new ComponentBuilder("[Copy to clipboard]").bold(true).color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, itemMeta.getAsString()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Copy the meta to your Clipboard"))).create();

        BaseComponent[] itemCopyButton = new ComponentBuilder("[Copy to clipboard]").bold(true).color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, material.toString()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Copy the meta to your Clipboard"))).create();

        BaseComponent[] messageComponent = new ComponentBuilder().append(metaCopyButton).append(itemCopyButton).create();

        p.spigot().sendMessage(messageComponent);
    }

    private void aboutMessageCommand(CommandSender p) {
        BaseComponent[] repoButton = new ComponentBuilder("[Click Here]\n").bold(true).color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/FlamingH0rse/CustomMobs"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Visit the repo"))).create();

        BaseComponent[] helpButton = new ComponentBuilder("[Click Here]\n").bold(true).color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/custommobs help"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("View Help"))).create();

        BaseComponent[] messageComponent = new ComponentBuilder("\n")
                .append("CustomMobs\n").color(net.md_5.bungee.api.ChatColor.AQUA).bold(true)
                .append(" \n")
                .append("An open-source plugin that enables users to \n").color(net.md_5.bungee.api.ChatColor.GOLD).bold(false)
                .append("create custom mobs with customizable health, \n").color(net.md_5.bungee.api.ChatColor.GOLD).bold(false)
                .append("damage and drops\n").color(net.md_5.bungee.api.ChatColor.GOLD).bold(false)
                .append(" \n")
                .append("Repository: ").color(net.md_5.bungee.api.ChatColor.BLUE).bold(true).append(repoButton)
                .append("Commands: ").event((ClickEvent) null).event((HoverEvent) null)
                .color(net.md_5.bungee.api.ChatColor.BLUE).bold(true).append(helpButton)
                .append("").create();

        p.spigot().sendMessage(messageComponent);
    }

    private void helpCommand(CommandSender p) {
        BaseComponent[] helpMessage = new ComponentBuilder("\n")
                .append("CustomMobs ").color(net.md_5.bungee.api.ChatColor.AQUA).bold(true)
                .append("[Commands]\n\n").color(net.md_5.bungee.api.ChatColor.DARK_RED)
                .append("/custommobs help").underlined(true).color(net.md_5.bungee.api.ChatColor.GOLD).bold(false)
                .append(" - Shows this message\n").underlined(false).color(net.md_5.bungee.api.ChatColor.WHITE)
                .append("/custommobs about").underlined(true).color(net.md_5.bungee.api.ChatColor.GOLD)
                .append(" - View plugin's about\n").underlined(false).color(net.md_5.bungee.api.ChatColor.WHITE)
                .append("/custommobs getiteminfo").underlined(true).color(net.md_5.bungee.api.ChatColor.GOLD)
                .append(" - Get item info including its meta to be used on config.yml\n").underlined(false).color(net.md_5.bungee.api.ChatColor.WHITE)
                .append("/custommobs spawnmob [Mob Internal Name]").underlined(true).color(net.md_5.bungee.api.ChatColor.GOLD)
                .append(" - Spawns your custom mob at your current location. Good for testing\n").underlined(false).color(net.md_5.bungee.api.ChatColor.WHITE).create();

        p.spigot().sendMessage(helpMessage);
    }
}
