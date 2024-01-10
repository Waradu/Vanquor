package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.utils.GameAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import dev.waradu.vanquor.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StartCommand implements CommandExecutor {

    GameAPI gameAPI = Main.getInstance().gameAPI;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0){
            gameAPI.start();
        } else{
            sender.sendMessage(Main.getPrefix(CommandTypes.COMMAND_USAGE_ERROR) + "§c/start");
        }

        int seconds = 10;
        new BukkitRunnable() {
            int remainingTime = seconds;

            @Override
            public void run() {
                if (remainingTime < 0) {
                    cancel();
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setLevel(remainingTime);
                    player.setExp(1);
                }

                remainingTime--;
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);

        return true;
    }
}
