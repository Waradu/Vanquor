package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MsgCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player receiver = Bukkit.getPlayer(args[0]);

        if (receiver == null) {
            sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_FEEDBACK) + "§cPlayer not found");
            return true;
        }

        sender.sendMessage(ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.DARK_GRAY + " > " + ChatColor.GREEN + receiver.getName() + ": " + ChatColor.WHITE + String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        receiver.sendMessage(ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.DARK_GRAY + " > " + ChatColor.GREEN + receiver.getName() + ": " + ChatColor.WHITE + String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

        return true;
    }
}
