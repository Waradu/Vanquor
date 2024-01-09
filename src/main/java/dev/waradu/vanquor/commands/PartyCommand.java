package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.Main;
import dev.waradu.vanquor.utils.GameAPI;
import dev.waradu.vanquor.utils.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PartyCommand implements CommandExecutor {

    GameAPI gameAPI = Main.getInstance().gameAPI;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("cr")) {
                    gameAPI.partyAPI.createParty((Player) sender);
                } else if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("j")) {

                } else if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("a")) {

                } else if (args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("d")) {

                } else if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("lv")) {

                } else if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("k")) {

                } else if (args[0].equalsIgnoreCase("disband") || args[0].equalsIgnoreCase("db")) {

                } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("in")) {
                    sender.sendMessage(gameAPI.partyAPI.getPartyByLeader((Player) sender).getName() + " info:");
                    sender.sendMessage(gameAPI.partyAPI.getPartyByLeader((Player) sender).getMembers().size() + " players:");
                    for (Player player : gameAPI.partyAPI.getPartyByLeader((Player) sender).getMembers()) {
                        sender.sendMessage("- " + player.getName());
                    }
                } else if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i")) {

                } else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {

                } else if (args[0].equalsIgnoreCase("chat") || args[0].equalsIgnoreCase("c")) {

                } else if (args[0].equalsIgnoreCase("settings") || args[0].equalsIgnoreCase("s")) {

                } else {
                    Party party = gameAPI.partyAPI.createParty((Player) sender);

                    List<Player> onlinePlayers = new ArrayList<>();
                    List<String> errorPlayers = new ArrayList<>();

                    for (String playerName : args) {
                        Player player = Bukkit.getPlayerExact(playerName);

                        if (player != null && player.isOnline() && !player.equals(sender)) {
                            onlinePlayers.add(player);
                        } else {
                            errorPlayers.add(playerName);
                        }
                    }

                    if (!onlinePlayers.isEmpty()) {
                        if (!errorPlayers.isEmpty()) {
                            sender.sendMessage("Failed to add " + String.join(", ", errorPlayers) + " to the party: not online.");
                        }
                        for (Player player : onlinePlayers) {
                            gameAPI.partyAPI.sendInvite(player, (Player) sender);
                            sender.sendMessage("Invited " + player.getName() + " to "+party.getName()+".");
                        }
                    }
                }
            } else {
                sender.sendMessage("/party [create|join|accept|deny|leave|kick|disband|info|invite|list|chat|settings] <args>");
            }
        }


        return true;
    }
}
