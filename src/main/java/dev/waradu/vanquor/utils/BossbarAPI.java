package dev.waradu.vanquor.utils;

import dev.waradu.vanquor.Main;
import org.bukkit.Bukkit;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;

import java.util.Iterator;

import static org.bukkit.Bukkit.getServer;

public class BossbarAPI {
    public void createBossBar(Player player) {
        BossBar bossBar = Bukkit.createBossBar("Health", BarColor.RED, BarStyle.SOLID);

        double initialProgress = player.getHealth() / player.getMaxHealth();
        bossBar.setProgress(initialProgress);

        bossBar.addPlayer(player);

        getServer().getScheduler().runTaskTimer(Main.getInstance(), () -> {
            double currentProgress = player.getHealth() / player.getMaxHealth();
            bossBar.setProgress(currentProgress);
        }, 0L, 20L);

        bossBar.setVisible(true);
    }

    public void removeBossBars() {
        Bukkit.broadcastMessage(Bukkit.getServer().getBossBars().toString());

        for (Player p : Bukkit.getOnlinePlayers()) {
            for (Iterator<KeyedBossBar> it = Bukkit.getServer().getBossBars(); it.hasNext(); ) {
                BossBar bar = it.next();
                Bukkit.broadcastMessage(bar.getTitle() + " - " + bar.getPlayers().toString());
                bar.removePlayer(p);
            }
        }
    }
}
