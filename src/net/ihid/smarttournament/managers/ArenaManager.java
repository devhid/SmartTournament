package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Mikey on 4/25/2016.
 */
public class ArenaManager {
    private final TournamentPlugin plugin;
    private final YamlConfiguration config;

    @Getter
    private List<Arena> arenas;

    public ArenaManager(TournamentPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.arenas = new ArrayList<>();
    }

    public void clearArenas() {
        arenas.clear();
    }

    public boolean isOccupied(Arena arena) {
        return arena.isOccupied();
    }

    public Arena getAvailableArena() {
        return arenas.stream().filter(arena -> !isOccupied(arena)).findAny().orElse(null);
    }

    public void setLocation(String arenaName, Player player, int position) {
        Location location = player.getLocation();
        String path = "arenas." + arenaName + "." + position;

        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getBlockX());
        config.set(path + ".y", location.getBlockY());
        config.set(path + ".z", location.getBlockZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());

        plugin.getRawConfig().saveConfig();
    }

    public void loadArenas() {
        for(String str: config.getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + str + ".";

            Location firstLocation = new Location(Bukkit.getWorld(config.getString(path + "1.world")),
                    config.getInt(path + "1.x"),
                    config.getInt(path + "1.y"),
                    config.getInt(path + "1.z"),
                    (float)config.getDouble(path + "1.yaw"),
                    (float)config.getDouble(path + "1.pitch"));

            Location secondLocation = new Location(Bukkit.getWorld(config.getString(path + "2.world")),
                    config.getInt(path + "2.x"),
                    config.getInt(path + "2.y"),
                    config.getInt(path + "2.z"),
                    (float)config.getDouble(path + "2.yaw"),
                    (float)config.getDouble(path + "2.pitch"));

            arenas.add(new Arena(str, firstLocation, secondLocation));
        }
    }
}
