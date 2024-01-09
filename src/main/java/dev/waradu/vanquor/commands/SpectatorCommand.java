package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import dev.waradu.vanquor.utils.GameAPI;
import org.bukkit.entity.Player;

public class SpectatorCommand implements CommandExecutor {

    GameAPI gameAPI = Main.getInstance().gameAPI;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_PERMISSION_ERROR) + "§cYou need to be an operator to use this command");
            return true;
        }

        if (args.length == 0) {
            Player target = (Player) sender;

            Boolean state = gameAPI.toggleSpectator(target);

            if (state) {
                sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_FEEDBACK) + "§aYou are now a spectator");
            } else {
                sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_FEEDBACK) + "§cYou are no longer a spectator");
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            Boolean state = gameAPI.toggleSpectator(target);

            if (state) {
                sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_FEEDBACK) + "§a" + target.getName() + "§ais now a spectator");
            } else {
                sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_FEEDBACK) + "§c" + target.getName() + "§cis no longer a spectator");
            }
        } else {
            sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_USAGE_ERROR) + "§c/spec <Player: Optional>");
        }

        return true;
    }

}
