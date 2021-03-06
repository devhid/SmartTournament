package net.ihid.smarttournament.listeners;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

class CrackShotDamageListener implements Listener {
    private final MainManager mainManager;

    CrackShotDamageListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();

        if(TournamentPlugin.getHookHandler().getCrackShotHook().isEnabled()) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onCrackShotDamage(WeaponDamageEntityEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if(!(evt.getVictim() instanceof Player)) {
                return;
            }

            if(!mainManager.isInTournament((Player) evt.getVictim())) {
                return;
            }

            Player player = (Player) evt.getVictim();

            switch (mainManager.getTournament().getStage()) {
                case WAITING:
                    if (player.getHealth() <= evt.getDamage()) {
                        evt.setCancelled(true);
                        evt.getVictim().teleport(mainManager.getSpectatorArea());
                    }

                    break;
                case ACTIVE:
                    mainManager.getMatches().stream()
                            .filter(match -> match.toSet().contains(player))
                            .findAny()
                            .ifPresent(match -> {
                                if (player.getHealth() > evt.getDamage()) {
                                    return;
                                }
                                evt.setCancelled(true);
                                player.teleport(mainManager.getWorldSpawn());

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
