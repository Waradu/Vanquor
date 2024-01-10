package dev.waradu.vanquor.listeners;

import dev.waradu.vanquor.Main;
import dev.waradu.vanquor.utils.BossbarAPI;
import dev.waradu.vanquor.utils.GameAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameListener implements Listener {

    private final Map<Player, Player> lastDamagerMap = new HashMap<>();

    GameAPI gameAPI = Main.getInstance().gameAPI;

    @EventHandler
    public void onSaturationChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            if (gameAPI.isSpectator((Player) e.getEntity())) {
                e.setCancelled(true);
                e.getEntity().setFoodLevel(20);
                e.getEntity().setSaturation(20);
            }
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (gameAPI.isSpectator((Player) e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (gameAPI.isSpectator(p)) {
            ItemStack item = e.getItemDrop().getItemStack().clone();
            e.getItemDrop().remove();
            p.getInventory().addItem(item);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (gameAPI.isSpectator(p)) {
            e.setCancelled(true);
        }
        if (gameAPI.isSpectator(p) && e.getItem() != null && e.getItem().getType() == Material.COMPASS) {
            openTeleportMenu(p);
        }
    }

    private void openTeleportMenu(Player player) {
        int count = ((Bukkit.getOnlinePlayers().size() - 1 + 8) / 9 * 9);
        Inventory menu = Bukkit.createInventory(null, count < 9 ? 9 : (Math.min(count, 54)), "Teleport Menu");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ItemStack head = getPlayerHead(onlinePlayer);

            if (onlinePlayer != player) {
                menu.addItem(head);
            }
        }

        player.openInventory(menu);
    }

    private ItemStack getPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName("§r"+player.getName());
        head.setItemMeta(meta);
        return head;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (gameAPI.isSpectator((Player) e.getWhoClicked()) && e.getView().getTitle().equals("Teleport Menu")) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player targetPlayer = Bukkit.getPlayer(((SkullMeta) e.getCurrentItem().getItemMeta()).getOwningPlayer().getUniqueId());
                if (targetPlayer != null) {
                    e.getWhoClicked().teleport(targetPlayer.getLocation());
                    e.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onBlockReceiveGame(BlockReceiveGameEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (gameAPI.isSpectator(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (gameAPI.isSpectator(p)) {
            gameAPI.setSpectator(p, false);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (false) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && gameAPI.isSpectator((Player) e.getEntity())) {
            e.setCancelled(true);
            return;
        }

        if (e.getDamager() instanceof Player && gameAPI.isSpectator((Player) e.getDamager())) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();
            Player lastDamager = e.getDamager() instanceof Player ? (Player) e.getDamager() : null;

            if (e.getDamage() >= 20) {
                e.setCancelled(true);
            }

            handleDamage(damaged, lastDamager, e.getDamage(), false);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && gameAPI.isSpectator((Player) e.getEntity())) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player) {
            Player damaged = (Player) e.getEntity();
            Player lastDamager = null;
            boolean isPlayer = false;

            if (e instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) e;
                if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                    if (gameAPI.isSpectator((Player) entityDamageByEntityEvent.getDamager())) {
                        e.setCancelled(true);
                        return;
                    } else {
                        lastDamager = (Player) entityDamageByEntityEvent.getDamager();
                    }
                    isPlayer = true;
                }
            } else {
                lastDamager = findLastDamager(damaged);
            }

            if (e.getDamage() >= 20) {
                e.setCancelled(true);
            }

            handleDamage(damaged, lastDamager, e.getDamage(), isPlayer);
        }
    }

    private void handleDamage(Player damaged, Player lastDamager, double damage, Boolean isPlayer) {
        Bukkit.broadcastMessage("calculating...");
        if ((damaged.getHealth() - damage) <= 0) {
            Bukkit.broadcastMessage(damaged.getName() + " should die:");

            if (lastDamager instanceof Player) {
                Bukkit.broadcastMessage("- " + lastDamager.getName() + " is Player");
            } else {
                Bukkit.broadcastMessage("- " + lastDamager.getName() + " is not Player");
            }
            handlePlayerDeath(damaged, lastDamager);
        } else if (isPlayer) {
            Bukkit.broadcastMessage("updating map:");
            if (lastDamager != null) {
                Bukkit.broadcastMessage("- adding");
                lastDamagerMap.put(damaged, lastDamager);
            } else {
                Bukkit.broadcastMessage("- removing");
                lastDamagerMap.remove(damaged);
            }
        } else {
            Bukkit.broadcastMessage("nothing");
        }
    }

    private Player findLastDamager(Player damaged) {
        return lastDamagerMap.get(damaged);
    }

    private void handlePlayerDeath(Player player, Player lastDamager) {
        player.setInvulnerable(true);
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setAllowFlight(true);
        player.teleport(player.getLocation().add(0, .1, 0));
        player.setFlying(true);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(Main.getInstance(), player);
        }

        Player closestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!player.equals(target) && !target.isInvulnerable()) {
                double distance = player.getLocation().distance(target.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPlayer = target;
                }
            }
        }

        String deathMessage;
        if (lastDamager != null) {
            deathMessage = Main.getPrefix() + "§9 Game §8 > §c" + player.getName() + " was killed by " + lastDamager.getName();
        } else if (closestPlayer != null) {
            deathMessage = Main.getPrefix() + "§9 Game §8 > §c" + player.getName() + " was killed by " + closestPlayer.getName() + " because he was closest";
        } else {
            deathMessage = Main.getPrefix() + "§9 Game §8 > §c" + player.getName() + " died with no possible killer";
        }

        Bukkit.broadcastMessage(deathMessage);
        player.sendMessage(Main.getPrefix() + "§9 Game §8 > §cYou're Out");

        lastDamagerMap.remove(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setHealth(20);
                player.setVelocity(new Vector(0, .2, 0));
            }
        }.runTaskLater(Main.getInstance(), 1);
    }
}
