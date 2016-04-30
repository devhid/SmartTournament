package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;
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
    private final Tournament[] tournaments = new Tournament[1];

    @Getter
    private final List<Player> players = new ArrayList<>();

    public boolean isInTournament(Player player) {
       return players.contains(player);
    }

    public Tournament getTournament() {
        return tournaments[0];
    }

    public void addTournament(Tournament tournament) {
        tournaments[0] = tournament;
    }


    public void startTournament() {
        if(!isTournamentRunning()) {
            getTournament().start();
        }
    }

    public void endTournament() {
        if(isTournamentRunning()) {
            getTournament().end();
        }
    }

    public boolean isTournamentRunning() {
        return getTournament() != null;
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
