package dev.waradu.vanquor;

import dev.waradu.vanquor.commands.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import dev.waradu.vanquor.utils.*;
import dev.waradu.vanquor.listeners.*;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    public GameAPI gameAPI = new GameAPI();

    private static Main instance;
    public static String Prefix = "§x§a§4§8§c§8§d§lVanquor §8> ";
    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Bukkit.getConsoleSender().sendMessage(Prefix + "Vanquor loaded");

        instance = this;

        gameAPI.init();

        getCommand("rl").setExecutor(new ReloadCommand());
        getCommand("spec").setExecutor(new SpectatorCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("bar").setExecutor(new BarCommand());
        getCommand("party").setExecutor(new PartyCommand());
        getCommand("msg").setExecutor(new MsgCommand());

        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new GameListener(), this);

        for (Player p : Bukkit.getOnlinePlayers()) {

            p.setScoreboard(gameAPI.createScoreboard());

            for (Player players : Bukkit.getOnlinePlayers()) {
                players.showPlayer(this, p);
            }
        }
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (gameAPI.isSpectator(p)) {
                gameAPI.removeSpectator(p);
            }
        }
    }

    public static String getPrefix() {
        return Prefix;
    }

    public static String getPrefix(String subprefix) {
        return Prefix + subprefix + " §8> ";
    }
}

