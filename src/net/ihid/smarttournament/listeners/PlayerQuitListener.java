package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Mikey on 7/21/2016.
 */
public class PlayerQuitListener implements Listener {
    private final MainManager mainManager;

    public PlayerQuitListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if(!mainManager.isInTournament(evt.getPlayer())) {
                return;
            }

            final Player player = evt.getPlayer();
            mainManager.removeFromTournament(player);

            if(mainManager.getTournament().getStage() == TournamentStage.ACTIVE) {
                mainManager.getMatches().stream()
                        .filter(match -> match.toSet().contains(player))
                        .findAny()
                        .ifPresent(match -> {
                            match.setWinner(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator());

                            mainManager.addMatchWinner(match.getWinner());
                            mainManager.endMatch(match);
                        });
            }
        }
    }
}
