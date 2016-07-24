package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Mikey on 7/21/2016.
 */
public class PlayerDamageListener implements Listener {
    private final MainManager mainManager;

    public PlayerDamageListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if (!(evt.getEntity() instanceof Player)) {
                return;
            }

            if(!mainManager.isInTournament((Player) evt.getEntity())) {
                return;
            }

            Player player = (Player) evt.getEntity();

            switch (mainManager.getTournament().getStage()) {
                case WAITING:
                    if (player.getHealth() <= evt.getFinalDamage()) {
                        evt.setCancelled(true);
                        evt.getEntity().teleport(mainManager.getSpectatorArea());
                    }
                    break;
                case ACTIVE:
                    mainManager.getMatches().stream()
                            .filter(match -> match.toSet().contains(player))
                            .findAny()
                            .ifPresent(match -> {
                                if (player.getHealth() > evt.getFinalDamage()) {
                                    return;
                                }
                                evt.setCancelled(true);

                                match.setWinner(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator());

                                mainManager.removeFromTournament(player);
                                mainManager.addMatchWinner(match.getWinner());
                                mainManager.endMatch(match);
                            });
                    break;
                default:
                    break;
            }
        }
    }
}
