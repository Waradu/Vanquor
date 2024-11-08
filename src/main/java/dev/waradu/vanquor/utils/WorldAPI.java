package dev.waradu.vanquor.utils;

import dev.waradu.vanquor.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class WorldAPI {
    public String teleport(String name, Player player) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return "World not found";
        }
        player.teleport(world.getSpawnLocation());
        return "Teleported to world";
    }

    public String createWorld(String name, WorldType type) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            return "World already exists";
        }
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            WorldCreator wc = new WorldCreator(name);

            wc.environment(World.Environment.NORMAL);
            wc.type(type);
            wc.createWorld();
        });

        return "World Created";
    }

    public String removeWorld(String name) {
        if (name.equals("world")) {
            return "You can't delete World";
        }
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return "World not found";
        }
        List<World> worlds = Bukkit.getWorlds();
        worlds.remove(world);
        World fallback = Bukkit.getWorld("world");
        if (fallback == null && !worlds.isEmpty()) {
            fallback = worlds.get(new Random().nextInt(worlds.size()));
        }
        if (fallback != null) {
            for (Player p : world.getPlayers()) {
                p.teleport(fallback.getSpawnLocation());
            }
        }

        if (deleteWorld(name)) {
            return "World Deleted";
        } else {
            return "Could not delete World";
        }
    }

    public String getAllWorldNames() {
        StringBuilder sb = new StringBuilder();
        List<World> worlds = Bukkit.getWorlds();
        for (int i = 0; i < worlds.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(worlds.get(i).getName());
        }
        return sb.toString();
    }

    public String loadWorld(String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            return "World already exists";
        }
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            WorldCreator wc = new WorldCreator(name);
            wc.createWorld();
        });

        return "World Loaded";
    }

    public String UnloadWorld(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return "World not found";
        }
        List<World> worlds = Bukkit.getWorlds();
        worlds.remove(world);
        World fallback = Bukkit.getWorld("world");
        if (fallback == null && !worlds.isEmpty()) {
            fallback = worlds.get(new Random().nextInt(worlds.size()));
        }
        if (fallback != null) {
            for (Player p : world.getPlayers()) {
                p.teleport(fallback.getSpawnLocation());
            }
        }

        if (Bukkit.unloadWorld(world, true)) {
            return "World Unloaded";
        } else {
            return "Could not unload World";
        }
    }

    public boolean deleteWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return false;
        }

        if (!Bukkit.unloadWorld(world, true)) {
            return false;
        }

        Path worldFolder = Paths.get(Bukkit.getWorldContainer().getAbsolutePath(), worldName);
        try {
            Files.walk(worldFolder)
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
