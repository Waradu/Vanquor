package dev.waradu.vanquor.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Party {
    private String name = "";
    private Player leader;
    private Boolean allowUninvitedPlayerJoin = false;
    private Boolean allowNonLeaderToInvite = true;
    private Color color = Color.WHITE;
    private Boolean isAdminParty = false;
    private final ArrayList<Player> members = new ArrayList<>();
    public final HashMap<Player, Player> playerInviteMap = new HashMap<>();
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLeader(Player leader) {
        this.leader = leader;
    }

    public Player getLeader() {
        return leader;
    }

    public ArrayList<Player> getMembers() {
        return members;
    }

    public void addMember(Player member) {
        members.add(member);
        if (!member.equals(leader))
            member.sendMessage("You have been added to "+getName()+".");

        for (Player player : members) {
            if (player.equals(member))
                continue;
            player.sendMessage(member.getName() + " has joined the party.");
        }
    }

    public void removeMember(Player member) {
        members.remove(member);
        member.sendMessage("You have been removed from "+getName()+".");

        for (Player player : members) {
            if (player.equals(member))
                continue;
            player.sendMessage(member.getName() + " has left the party.");
        }

        if (leader.equals(member)) {
            leader = members.get(0);
            leader.sendMessage("You are now the leader of the party.");
        }

        for (Player player : members) {
            player.sendMessage(leader.getName() + " is now the leader of the party.");
        }
    }
}
