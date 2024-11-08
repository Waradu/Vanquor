package dev.waradu.vanquor.utils;

import dev.waradu.vanquor.models.Party;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PartyAPI {
    private final HashMap<UUID, Party> playerPartyMap = new HashMap<>();

    public void sendInvite(Player player, Player leader) {
        getPartyByLeader(leader).playerInviteMap.put(player.getUniqueId(), leader.getUniqueId());

        TextComponent accept = new TextComponent("Accept");
        accept.setColor(ChatColor.GREEN);
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + leader.getName()));

        TextComponent space = new TextComponent(" ");

        TextComponent deny = new TextComponent("Deny");
        deny.setColor(ChatColor.RED);
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + leader.getName()));

        BaseComponent[] combinedComponents = new BaseComponent[]{accept, space, deny};

        player.spigot().sendMessage(combinedComponents);
    }

    public Boolean acceptInvite(Player player, Player leader) {
        try {
            if (getPartyByLeader(leader).playerInviteMap.get(player.getUniqueId()) == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        getPartyByLeader(leader).addMember(player);
        getPartyByLeader(leader).playerInviteMap.remove(player.getUniqueId());

        return true;
    }

    public Boolean declineInvite(Player player, Player leader) {
        try {
            if (getPartyByLeader(leader).playerInviteMap.get(player.getUniqueId()) == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        getPartyByLeader(leader).playerInviteMap.remove(player.getUniqueId());
        return true;
    }

    public Party getPartyByPlayer(Player player) {
        for (Party party : playerPartyMap.values()) {
            if (party.getMembers().contains(player)) {
                return party;
            }
        }
        return null;
    }

    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public Party createParty(Player leader) {
        if (playerPartyMap.containsKey(leader.getUniqueId())) {
            return getPartyByLeader(leader);
        }

        Party party = new Party();
        party.setLeader(leader);
        party.addMember(leader);
        party.setName(leader.getName() + "'s Party");
        playerPartyMap.put(leader.getUniqueId(), party);

        return party;
    }

    public void joinParty(Player player, Player leader) {
        playerPartyMap.get(leader.getUniqueId()).addMember(player);
        playerPartyMap.put(player.getUniqueId(), playerPartyMap.get(leader.getUniqueId()));
    }

    public Boolean leaveParty(Player player) {
        try {
            getPartyByPlayer(player).removeMember(player);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    public Party getPartyByLeader(Player leader) {
        return playerPartyMap.get(leader.getUniqueId());
    }

    public String getPartyName(Player player) {
        return playerPartyMap.get(player.getUniqueId()).getName();
    }

    public ArrayList<Player> getPartyMembers(Player player) {
        return playerPartyMap.get(player.getUniqueId()).getMembers();
    }
}
