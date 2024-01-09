package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.Main;
import dev.waradu.vanquor.utils.GameAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class BarCommand implements CommandExecutor {

    GameAPI gameAPI = Main.getInstance().gameAPI;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && Objects.equals(args[0], "remove")){
            gameAPI.bossbarAPI.removeBossBars();
        } else{
            sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_USAGE_ERROR) + "§c/bar remove");
        }

        return true;
    }

}
