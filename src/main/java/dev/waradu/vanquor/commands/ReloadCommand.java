package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import dev.waradu.vanquor.Main;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0){
            Bukkit.broadcastMessage(Main.getPrefix() + ChatColor.RED+"Reloading server");
            Bukkit.getServer().reload();
            Bukkit.broadcastMessage(Main.getPrefix() + "§aReload complete!");
        } else{
            sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_USAGE_ERROR) + ChatColor.RED+"/rl");
        }

        return true;
    }

}
