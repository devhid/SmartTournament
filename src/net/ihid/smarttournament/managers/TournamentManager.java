package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikey on 4/25/2016.
 */
public class TournamentManager {

    @Getter
    private Tournament tournament;

    @Getter
    private final List<Player> players = new ArrayList<>();

    public TournamentManager() {
        tournament = new Tournament();
    }

    public boolean isInTournament(Player player) {
       return players.contains(player);
    }

    public void startTournament() {
        if(!isTournamentRunning()) {
            tournament.start();
        }
    }

    public void endTournament() {
        if(isTournamentRunning()) {
            tournament.end();
        }
    }

    public boolean isTournamentRunning() {
        return tournament.getStage() != TournamentStage.NON_ACTIVE;
    }

    public void setSpectatorArea(Player player) {
        final YamlConfiguration config = TournamentPlugin.i.getConfig();
        final String path = "spectator";

        Location loc = player.getLocation();
        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", loc.getBlockX());
        config.set(path + ".y", loc.getBlockY());
        config.set(path + ".z", loc.getBlockZ());
        config.set(path + ".pitch", loc.getPitch());
        config.set(path + ".yaw", loc.getYaw());

        TournamentPlugin.i.getRawConfig().saveConfig();

    }

    public Location getSpectatorArea() {
        final YamlConfiguration config = TournamentPlugin.i.getConfig();
        final String path = "spectator.";

        return new Location(Bukkit.getWorld(config.getString(path + "world")),
                config.getInt(path + "x"),
                config.getInt(path + "y"),
                config.getInt(path + "z"),
                config.getInt(path + "yaw"),
                config.getInt(path + "pitch"));
    }
}
