package net.ihid.smarttournament.objects;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.ArenaManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Arena {
    private ArenaManager arenaManager = TournamentPlugin.getMainManager().getArenaManager();

    @Getter
    private String arenaName;

    @Getter @Setter
    private Location firstLoc, secondLoc;

    @Setter @Getter
    private boolean occupied;

    public Arena(String arenaName, Location firstLoc, Location secondLoc) {
        this.arenaName = arenaName;
        this.firstLoc = firstLoc;
        this.secondLoc = secondLoc;

        arenaManager.addArena(this);
    }

    public Set<Location> toSet() {
        return Sets.newHashSet(firstLoc, secondLoc);
    }

    public void setLocation(Player player, int num) {
        final Location loc = player.getLocation();
        YamlConfiguration config = TournamentPlugin.i.getConfig();
        String path = "arenas." + arenaName + "." + num;

        config.set(path + ".world", loc.getWorld());
        config.set(path + ".x", loc.getBlockX());
        config.set(path + ".y", loc.getBlockY());
        config.set(path + ".z", loc.getBlockZ());
        config.set(path + ".pitch", loc.getPitch());
        config.set(path + ".yaw", loc.getYaw());

        TournamentPlugin.i.getRawConfig().saveConfig();
    }
}
