package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.api.TournamentAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Mikey on 7/21/2016.
 */
public class PlayerQuitListener implements Listener {
    private final TournamentAPI tournamentAPI;

    public PlayerQuitListener(TournamentPlugin plugin) {
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        if(tournamentAPI.isTournamentRunning()) {
            if(!tournamentAPI.isInTournament(evt.getPlayer())) {
                return;
            }

            final Player player = evt.getPlayer();
            tournamentAPI.removeFromTournament(player);

            if(tournamentAPI.getTournament().getStage() == TournamentStage.ACTIVE) {
                tournamentAPI.getMatches().stream()
                        .filter(match -> match.toSet().contains(player))
                        .findAny()
                        .ifPresent(match -> {
                            match.setWinner(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator());

                            tournamentAPI.addMatchWinner(match.getWinner());
                            tournamentAPI.endMatch(match);
                        });
            }
        }
    }
}
