package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.Main;
import dev.waradu.vanquor.utils.WorldAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {

    WorldAPI worldAPI = new WorldAPI();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("create")) {

                if (args.length < 3) {
                    sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.RED + "Invalid usage");
                    return false;
                }

                WorldType worldType = WorldType.valueOf(args[2]);

                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + "Creating World");
                String result = worldAPI.createWorld(args[1], worldType);
                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + result);
            } else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + "Teleporting player");
                if (args.length == 2) {
                    String result = worldAPI.teleport(args[1], (Player) sender);
                    sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + result);
                    return true;
                } else if  (args.length == 3) {
                    Player player = Bukkit.getPlayerExact(args[2]);
                    String result = worldAPI.teleport(args[1], player);
                    player.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + result);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length < 2) {
                    sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.RED + "Invalid usage");
                    return false;
                }

                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + "Deleting World");
                String result = worldAPI.removeWorld(args[1]);
                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + result);
            } else  if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + "Creating World");
                String result = worldAPI.getAllWorldNames();
                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.GREEN + result);
                return true;
            } else {
                sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.RED + "Invalid usage");
                return false;
            }
        } else {
            sender.sendMessage(Main.getPrefix(CommandTypes.WORLD) + ChatColor.RED + "Invalid usage");
            return false;
        }

        return true;
    }

}
