package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class TournamentManager {
    private final MainManager mainManager;
    private final TournamentPlugin plugin;
    private final YamlConfiguration config;

    @Getter
    private Tournament tournament;

    @Getter
    private List<UUID> participants, original;

    public TournamentManager(MainManager mainManager) {
        this.plugin = TournamentPlugin.getInstance();
        this.mainManager = mainManager;
        this.config = plugin.getConfig();
        this.participants = new ArrayList<>();
        this.original = new ArrayList<>();
    }

    public List<UUID> getOriginalParticipants() {
        return original;
    }

    public void clearParticipants() {
        participants.clear();
    }

    public boolean isInTournament(Player player) {
        return participants.contains(player.getUniqueId()) || mainManager.getMatchWinners().contains(player.getUniqueId()) || mainManager.getMatches().stream().filter(match -> match.toSet().contains(player)).count() > 0;
    }

    public void addParticipant(Player player) {
        participants.add(player.getUniqueId());
    }

    public void removeFromTournament(Player... participants) {
        for(Player player: participants) {
            if (mainManager.getMatchWinners().contains(player.getUniqueId())) {
                mainManager.getMatchWinners().remove(player.getUniqueId());
            }

            if (mainManager.getParticipants().contains(player.getUniqueId())) {
                mainManager.getParticipants().remove(player.getUniqueId());
            }
        }
    }

    public void startTournament() {
        if(!isTournamentRunning()) {
            tournament = new Tournament();
            tournament.start(false);
        }
    }

    public void endTournament() {
        if(isTournamentRunning()) {
            tournament.end();
        }
    }

    public boolean isTournamentRunning() {
        return tournament != null && (tournament.getStage() != TournamentStage.NON_ACTIVE);
    }
    
    public void setWorldSpawn(Player player) {
        final Location loc = player.getLocation();

        config.set("world-spawn.world", loc.getWorld().getName());
        config.set("world-spawn.x", loc.getBlockX());
        config.set("world-spawn.y", loc.getBlockY());
        config.set("world-spawn.z", loc.getBlockZ());
        config.set("world-spawn.yaw", loc.getYaw());
        config.set("world-spawn.pitch", loc.getPitch());

        plugin.getRawConfig().saveConfig();
    }
    
    public Location getWorldSpawn() {
        return new Location(Bukkit.getWorld(config.getString("world-spawn.world")),
                config.getInt("world-spawn.x"),
                config.getInt("world-spawn.y"),
                config.getInt("world-spawn.z"),
                (float) config.getDouble("world-spawn.yaw"),
                (float) config.getDouble("world-spawn.pitch"));
    }

    public void setSpectatorArea(Location location) {
        config.set("spectator.world", location.getWorld().getName());
        config.set("spectator.x", location.getBlockX());
        config.set("spectator.y", location.getBlockY());
        config.set("spectator.z", location.getBlockZ());
        config.set("spectator.yaw", location.getYaw());
        config.set("spectator.pitch", location.getPitch());

        plugin.getRawConfig().saveConfig();
    }

    public Location getSpectatorArea() {
        return new Location(Bukkit.getWorld(config.getString("spectator.world")),
                config.getInt("spectator.x"),
                config.getInt("spectator.y"),
                config.getInt("spectator.z"),
                (float) config.getDouble("spectator.yaw"),
                (float) config.getDouble("spectator.pitch"));
    }
}
