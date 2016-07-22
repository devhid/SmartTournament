package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Mikey on 7/21/2016.
 */
public class PlayerDamageListener implements Listener {
    private final TournamentAPI tournamentAPI;

    public PlayerDamageListener(TournamentPlugin plugin) {
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if(tournamentAPI.isTournamentRunning()) {
            if (!(evt.getEntity() instanceof Player)) {
                return;
            }

            if(!tournamentAPI.isInTournament((Player) evt.getEntity())) {
                return;
            }

            Player player = (Player) evt.getEntity();

            switch (tournamentAPI.getTournament().getStage()) {
                case WAITING:
                    if (player.getHealth() <= evt.getFinalDamage()) {
                        evt.setCancelled(true);
                        evt.getEntity().teleport(tournamentAPI.getSpectatorArea());
                    }
                    break;
                case ACTIVE:
                    tournamentAPI.getMatches().stream()
                            .filter(match -> match.toSet().contains(player))
                            .findAny()
                            .ifPresent(match -> {
                                if (player.getHealth() > evt.getFinalDamage()) {
                                    return;
                                }
                                evt.setCancelled(true);

                                match.setWinner(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator());

                                tournamentAPI.removeFromTournament(player);
                                tournamentAPI.addMatchWinner(match.getWinner());
                                tournamentAPI.endMatch(match);
                            });
                    break;
                default:
                    break;
            }
        }
    }
}
