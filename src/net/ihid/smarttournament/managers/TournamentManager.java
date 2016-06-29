package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.config.Lang;
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
    private TournamentPlugin plugin = TournamentPlugin.getInstance();

    @Getter
    private Tournament tournament;

    @Getter
    private final List<Player> players = new ArrayList<>();

    public TournamentManager() {
        tournament = new Tournament();
    }

    public boolean isInTournament(Player player) {
        return players.contains(player) || TournamentPlugin.getTournamentAPI().getWinners().contains(player) || TournamentPlugin.getTournamentAPI().getMatches().stream().filter(match -> match.toSet().contains(player)).count() > 0;
    }

    public void addToTournament(Player player) {
        players.add(player);
    }

    public void removeFromTournament(Player player) {
        TournamentAPI api = TournamentPlugin.getTournamentAPI();

        if (api.getWinners().contains(player)) {
            api.getWinners().remove(player);
        }

        if (api.getPlayers().contains(player)) {
            api.getPlayers().remove(player);
        }

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
    
    public void setWorldSpawn(Player player) {
        YamlConfiguration config = plugin.getConfig();
        Location loc = player.getLocation();

        config.set("world-spawn.world", loc.getWorld().getName());
        config.set("world-spawn.x", loc.getBlockX());
        config.set("world-spawn.y", loc.getBlockY());
        config.set("world-spawn.z", loc.getBlockZ());
        config.set("world-spawn.yaw", loc.getYaw());
        config.set("world-spawn.pitch", loc.getPitch());

        plugin.getRawConfig().saveConfig();
    }
    
    public Location getWorldSpawn() {
        YamlConfiguration config = plugin.getConfig();

        return new Location(Bukkit.getWorld(config.getString("world-spawn.world")),
                config.getInt("world-spawn.x"),
                config.getInt("world-spawn.y"),
                config.getInt("world-spawn.z"),
                (float) config.getDouble("world-spawn.yaw"),
                (float) config.getDouble("world-spawn.pitch"));
    }

    public void setSpectatorArea(Player player) {
        YamlConfiguration config = plugin.getConfig();
        Location loc = player.getLocation();

        config.set("spectator.world", loc.getWorld().getName());
        config.set("spectator.x", loc.getBlockX());
        config.set("spectator.y", loc.getBlockY());
        config.set("spectator.z", loc.getBlockZ());
        config.set("spectator.yaw", loc.getYaw());
        config.set("spectator.pitch", loc.getPitch());

        plugin.getRawConfig().saveConfig();
    }

    public Location getSpectatorArea() {
        YamlConfiguration config = plugin.getConfig();

        return new Location(Bukkit.getWorld(config.getString("spectator.world")),
                config.getInt("spectator.x"),
                config.getInt("spectator.y"),
                config.getInt("spectator.z"),
                (float) config.getDouble("spectator.yaw"),
                (float) config.getDouble("spectator.pitch"));
    }
}
