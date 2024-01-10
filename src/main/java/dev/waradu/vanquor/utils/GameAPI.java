package dev.waradu.vanquor.utils;

import dev.waradu.vanquor.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameAPI {

    public BossbarAPI bossbarAPI = new BossbarAPI();
    public PartyAPI partyAPI = new PartyAPI();

    private final Map<Player, Player> lastDamagerMap = new HashMap<>();
    private final Map<Player, Set<Player>> killsMap = new HashMap<>();
    private int teamSize = 1;
    private final Map<Player,
            ItemStack[]> inventories = new HashMap<>();
    private final Map<Player, Boolean> spectator = new HashMap<>();

    public static Scoreboard createScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", Criteria.DUMMY, "scoreboard");

        objective.setDisplayName("Custom Scoreboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int counter = 0;

        counter += Bukkit.getOnlinePlayers().size() + 1;

        Score first = objective.getScore("Killz:");
        first.setScore(counter);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Score score = objective.getScore("0" + " - " + player.getName());
            counter--;
            score.setScore(counter);
        }

        return scoreboard;
    }

    public void start() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isSpectator(p)) {
                removeSpectator(p);
            }
        }

        int numPlayers = Bukkit.getServer().getOnlinePlayers().size();
        double angleIncrement = 2 * Math.PI / numPlayers;

        double x = -32.5;
        double y = 74;
        double z = 29.5;
        double radius = 3;

        Location center = new Location(Bukkit.getServer().getWorld("world"), x, y, z);

        int playerCount = 0;
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            double angle = angleIncrement * playerCount;
            double x2 = center.getX() + radius * Math.cos(angle);
            double z2 = center.getZ() + radius * Math.sin(angle);

            Location teleportLocation = new Location(center.getWorld(), x2, center.getY(), z2);
            teleportLocation.setDirection(center.toVector().subtract(teleportLocation.toVector()));
            player.teleport(teleportLocation);

            playerCount++;
        }
    }

    public void addKillToPlayer(Player killer, Player victim) {
        Set<Player> kills = killsMap.computeIfAbsent(killer, k -> new HashSet<>());

        kills.add(victim);
    }

    public void removeKillFromPlayer(Player killer, Player victim) {
        Set<Player> kills = killsMap.get(killer);

        kills.remove(victim);
    }

    public void removeAllKillsFromPlayer(Player player) {
        killsMap.remove(player);
    }

    public Set<Player> getAllKillsFromPlayer(Player player) {
        return killsMap.get(player);
    }

    public void setSpectator(Player player) {
        setCantPush(player);

        spectator.put(player, true);

        player.closeInventory();
        player.setInvulnerable(true);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setAllowFlight(true);
        player.teleport(player.getLocation().add(0, .1, 0));
        player.setFlying(true);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false, false));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(Main.getInstance(), player);
        }

        inventories.put(player, player.getInventory().getContents().clone());

        player.getInventory().clear();

        player.getInventory().setArmorContents(null);

        ItemStack compass = new ItemStack(Material.COMPASS);

        ItemMeta meta = compass.getItemMeta();

        meta.setDisplayName("§r"+"Teleport to player");

        compass.setItemMeta(meta);
        player.getInventory().setItem(0, compass);

        player.addScoreboardTag("vanquor.spec");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setHealth(20);
                player.setVelocity(new Vector(0, .2, 0));
            }
        }.runTaskLater(Main.getInstance(), 1);

    }

    public void removeSpectator(Player player) {
        setCanPush(player);

        spectator.remove(player);

        player.setInvulnerable(false);
        player.setAllowFlight(false);
        player.setFlying(false);

        player.removeScoreboardTag("vanquor.spec");
        player.getInventory().setContents(inventories.get(player));

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(Main.getInstance(), player);
        }
    }

    public Boolean isSpectator(Player player) {
        return spectator.getOrDefault(player, false) || player.getScoreboardTags().contains("vanquor.spec");
    }

    public Boolean toggleSpectator(Player player) {
        if (isSpectator(player)) {
            removeSpectator(player);
        } else {
            setSpectator(player);
        }
        return isSpectator(player);
    }

    public void setCantPush(Player p) {
        Team team = p.getScoreboard().getTeam("Vanished");
        if (team == null) {
            team = p.getScoreboard().registerNewTeam("Vanished");
        }
        try {
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.addEntry(p.getName());
        } catch (NoSuchMethodError | NoClassDefFoundError ignored) {
        }
    }

    public void setCanPush(Player p) {
        Team team = p.getScoreboard().getTeam("Vanished");
        if (team != null)
            team.removeEntry(p.getName());
    }
}