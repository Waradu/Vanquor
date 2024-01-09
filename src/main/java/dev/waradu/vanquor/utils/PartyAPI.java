package dev.waradu.vanquor.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PartyAPI {
    private final HashMap<Player, Party> playerPartyMap = new HashMap<>();

    public void sendInvite(Player player, Player leader) {
        getPartyByLeader(leader).playerInviteMap.put(player, leader);

        TextComponent accept = new TextComponent("Accept");
        accept.setColor(ChatColor.GREEN);
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));

        TextComponent space = new TextComponent(" ");

        TextComponent deny = new TextComponent("deny");
        deny.setColor(ChatColor.GREEN);
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));

        player.spigot().sendMessage(accept);
    }

    public void acceptInvite(Player player, Player leader) {
        if (getPartyByLeader(leader).playerInviteMap.get(player) == null) {
            return;
        }
        getPartyByLeader(leader).addMember(player);
        getPartyByLeader(leader).playerInviteMap.remove(player);
    }

    public void declineInvite(Player player, Player leader) {
        if (getPartyByLeader(leader).playerInviteMap.get(player) == null) {
            return;
        }
        getPartyByLeader(leader).playerInviteMap.remove(player);
    }
    public Party createParty(Player leader) {
        if (playerPartyMap.containsKey(leader)) {
            return getPartyByLeader(leader);
        }

        Party party = new Party();
        party.setLeader(leader);
        party.addMember(leader);
        party.setName(leader.getName() + "'s Party");
        playerPartyMap.put(leader, party);

        return party;
    }

    public void joinParty(Player player, Player leader) {
        playerPartyMap.get(leader).addMember(player);
        playerPartyMap.put(player, playerPartyMap.get(leader));
    }

    public void leaveParty(Player player) {
        playerPartyMap.get(player).removeMember(player);
        playerPartyMap.remove(player);
    }

    public Party getPartyByLeader(Player leader) {
        return playerPartyMap.get(leader);
    }

    public String getPartyName(Player player) {
        return playerPartyMap.get(player).getName();
    }

    public ArrayList<Player> getPartyMembers(Player player) {
        return playerPartyMap.get(player).getMembers();
    }
}
