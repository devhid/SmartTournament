package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.objects.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikey on 4/25/2016.
 */
public class ArenaManager {
    @Getter
    private List<Arena> arenas = new ArrayList<>();

    public boolean isOccupied(Arena arena) {
        return arena.isOccupied();
    }

    public Arena getAvailableArena() {
        for(Arena arena: arenas) {
            if(!isOccupied(arena)) {
                return arena;
            }
        }
        return null;
    }

    public void setLocation(String arenaName, Player player, int num) {
        final YamlConfiguration config = TournamentPlugin.i.getConfig();
        final String path = "arenas." + arenaName + "." + num;

        Location loc = player.getLocation();
        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", loc.getBlockX());
        config.set(path + ".y", loc.getBlockY());
        config.set(path + ".z", loc.getBlockZ());
        config.set(path + ".pitch", loc.getPitch());
        config.set(path + ".yaw", loc.getYaw());

        TournamentPlugin.i.getRawConfig().saveConfig();
    }

    public void loadArenas() {
        YamlConfiguration config = TournamentPlugin.i.getConfig();
        
        for(String str: config.getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + str + ".";

            Location firstLoc = new Location(Bukkit.getWorld(config.getString(path + "1.world")),
                    config.getInt(path + "1.x"),
                    config.getInt(path + "1.y"),
                    config.getInt(path + "1.z"),
                    config.getInt(path + "1.pitch"),
                    config.getInt(path + "1.yaw"));

            Location secondLoc = new Location(Bukkit.getWorld(config.getString(path + "2.world")),
                    config.getInt(path + "2.x"),
                    config.getInt(path + "2.y"),
                    config.getInt(path + "2.z"),
                    config.getInt(path + "2.pitch"),
                    config.getInt(path + "2.yaw"));
            arenas.add(new Arena(str, firstLoc, secondLoc));
        }
    }
}