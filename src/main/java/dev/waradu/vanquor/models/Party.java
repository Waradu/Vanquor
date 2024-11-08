package dev.waradu.vanquor.models;

import dev.waradu.vanquor.CommandTypes;
import dev.waradu.vanquor.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Party {
    private String name = "";
    private UUID leader;
    private Boolean allowUninvitedPlayerJoin = false;
    private Boolean allowNonLeaderToInvite = true;
    private Color color = Color.WHITE;
    private Boolean isAdminParty = false;
    private final ArrayList<UUID> members = new ArrayList<>();
    public final HashMap<UUID, UUID> playerInviteMap = new HashMap<>();
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLeader(Player leader) {
        this.leader = leader.getUniqueId();
    }

    public Player getLeader() {
        return Bukkit.getPlayer(this.leader);
    }

    public ArrayList<Player> getMembers() {
        ArrayList<Player> playerList = new ArrayList<>();

        Player leaderPlayer = getLeader();
        if (leaderPlayer != null) {
            playerList.add(leaderPlayer);
        }

        for (UUID uuid : members) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null && !player.equals(leaderPlayer)) {
                playerList.add(player);
            }
        }

        return playerList;
    }

    public void addMember(Player member) {
        members.add(member.getUniqueId());
    }

    public void removeMember(Player member) {
        Player leader = Bukkit.getPlayer(this.leader);

        members.remove(member.getUniqueId());

        if (leader != null && leader.equals(member)) {
            this.leader = members.get(0);
            leader.sendMessage(Main.getPrefix(CommandTypes.PARTY) + "§6You are now the leader of the party.");

            for (Player player : getMembers()) {
                player.sendMessage(Main.getPrefix(CommandTypes.PARTY) + "§a" + leader.getName() + " is now the leader of the party.");
            }
        }
    }
}
