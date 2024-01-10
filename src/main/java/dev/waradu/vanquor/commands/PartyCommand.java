package dev.waradu.vanquor.commands;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.Main;
import dev.waradu.vanquor.utils.GameAPI;
import dev.waradu.vanquor.utils.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PartyCommand implements CommandExecutor {

    GameAPI gameAPI = Main.getInstance().gameAPI;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("chat") || args[0].equalsIgnoreCase("c") || label.equals("pc")) {
                    Party party = gameAPI.partyAPI.getPartyByPlayer((Player) sender);

                    if (party == null) {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You are not in a party.");
                        return true;
                    }

                    if (label.equals("pc")) {
                        for (Player member : party.getMembers()) {
                            member.sendMessage(CommandTypes.PARTY + " §7" + sender.getName() + ": §f" + String.join(" ", args));
                        }

                        return true;
                    }

                    for (Player member : party.getMembers()) {
                        member.sendMessage(CommandTypes.PARTY + " §7" + sender.getName() + ": §f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                    }
                } else if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("cr")) {
                    gameAPI.partyAPI.createParty((Player) sender);

                    sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + "§aYou created a party.");
                } else if (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("j")) {

                } else if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("a")) {
                    Player player = Bukkit.getPlayerExact(args[1]);

                    if (player != null && player.isOnline()) {
                        boolean success = gameAPI.partyAPI.acceptInvite((Player) sender, player);
                        if (success) {
                            sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + "§aYou joined " + gameAPI.partyAPI.getPartyByLeader(player).getName() + ".");
                        } else {
                            sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You do not have an invite from " + player.getName() + ".");
                        }
                    } else {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "Player " + args[1] + " is not online.");
                    }
                } else if (args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("d")) {
                    Player player = Bukkit.getPlayerExact(args[1]);

                    if (player != null && player.isOnline()) {
                        boolean success = gameAPI.partyAPI.declineInvite((Player) sender, player);
                        if (success) {
                            sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + "§aYou denied the invite to " + gameAPI.partyAPI.getPartyByLeader(player).getName() + ".");
                        } else {
                            sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You do not have an invite from " + player.getName() + ".");
                        }
                    } else {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "Player " + args[1] + " is not online.");
                    }
                } else if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("lv")) {
                    Boolean success = gameAPI.partyAPI.leaveParty((Player) sender);
                    if (success) {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + "§aYou left the party.");
                    } else {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You are not in a party.");
                    }
                } else if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("k")) {
                    Party party = gameAPI.partyAPI.getPartyByPlayer((Player) sender);

                    Player player = Bukkit.getPlayerExact(args[1]);

                    if (player != null && player.isOnline() && party.getLeader().equals((Player) sender)) {
                        if (party.getLeader().equals(player)) {
                            sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You can't kick yourself.");
                            return true;
                        }
                        if (gameAPI.partyAPI.leaveParty(player)) {
                            sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.GREEN + "You kicked " + player.getName() + " from the party.");
                            player.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You were kicked from the party.");
                        } else {
                            sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "Player " + player.getName() + " is not in your party.");
                        }
                    } else {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "Player " + args[1] + " is not online.");
                    }
                } else if (args[0].equalsIgnoreCase("disband") || args[0].equalsIgnoreCase("db")) {
                    Party party = gameAPI.partyAPI.getPartyByPlayer((Player) sender);

                    if (party == null) {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You are not in a party.");
                        return true;
                    }

                    if (!party.getLeader().equals(sender)) {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You are not the leader of the party.");
                        return true;
                    }

                    sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.GREEN + "You disbanded the party.");
                    for (Player member : party.getMembers()) {
                        gameAPI.partyAPI.leaveParty(member);

                        member.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "The Party was disbanded.");

                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("in")) {
                    StringBuilder actionBarText = new StringBuilder();


                    Party party = gameAPI.partyAPI.getPartyByPlayer((Player) sender);

                    if (party == null) {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You are not in a party.");
                        return true;
                    }

                    actionBarText.append(ChatColor.WHITE + party.getName() + ": ");
                    for (Player member : party.getMembers()) {
                        if (member == party.getLeader()) {
                            actionBarText.append(ChatColor.AQUA).append(member.getName()).append(ChatColor.RESET);
                        } else {
                            actionBarText.append(", ").append(member.getName());
                        }
                    }


                    gameAPI.partyAPI.sendActionBar((Player) sender, actionBarText.toString());
                } else if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("i")) {
                    Party party = gameAPI.partyAPI.createParty((Player) sender);

                    List<Player> onlinePlayers = new ArrayList<>();
                    List<String> errorPlayers = new ArrayList<>();

                    args = Arrays.copyOfRange(args, 1, args.length);

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
                            sender.sendMessage("Invited " + player.getName() + " to " + party.getName() + ".");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {
                    Party party = gameAPI.partyAPI.getPartyByPlayer((Player) sender);

                    if (party == null) {
                        sender.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.RED + "You are not in a party.");
                        return true;
                    }

                    String playerList = party.getMembers().stream()
                            .map(player -> (player.equals(party.getLeader()) ? ChatColor.AQUA : "") + player.getName() + (player.equals(party.getLeader()) ? ChatColor.RESET : ""))
                            .collect(Collectors.joining(", "));

                    String message = Main.getPrefix(CommandTypes.PARTY) + ChatColor.WHITE + playerList;
                    sender.sendMessage(message);

                } else if (args[0].equalsIgnoreCase("settings") || args[0].equalsIgnoreCase("s")) {
                    Party party = gameAPI.partyAPI.getPartyByPlayer((Player) sender);
                    Player leader = party.getLeader();

                    if (leader != (Player) sender) {
                        return false;
                    }

                    if (Objects.equals(args[1], "name")) {

                        String name = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                        party.setName(name);

                        for (Player member : party.getMembers()) {
                            member.sendMessage(Main.getPrefix(CommandTypes.PARTY) + ChatColor.GRAY + "Party name set to " + name + ".");
                        }

                        return true;
                    }
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
                            sender.sendMessage("Invited " + player.getName() + " to " + party.getName() + ".");
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
