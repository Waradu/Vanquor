package dev.waradu.vanquor.utils;

import dev.waradu.vanquor.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigAPI {
    public static File config;
    public static FileConfiguration settings;

    public static void createConfig() {
        config = new File(Main.getInstance().getDataFolder(), "config.yml");
        if (!config.exists()) {
            config.getParentFile().mkdirs();
            Main.getInstance().saveResource("config.yml", false);
        }

        settings = new YamlConfiguration();
        try {
            settings.load(config);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try{
            settings.save(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
