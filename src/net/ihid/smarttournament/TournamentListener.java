package net.ihid.smarttournament;

import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.api.events.MatchEndEvent;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class TournamentListener implements Listener {
    private TournamentPlugin plugin;
    private TournamentAPI tournamentAPI;

    public TournamentListener(TournamentPlugin instance) {
        this.plugin = instance;
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        manageEvent(evt, evt.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if(!(evt.getEntity() instanceof Player)) {
            return;
        }
        manageEvent(evt, (Player) evt.getEntity());
    }

    private void manageEvent(Event evt, Player player) {
        if(!tournamentAPI.isTournamentRunning()) {
            return;
        }

        if(!tournamentAPI.isInTournament(player)) {
            return;
        }

        switch(tournamentAPI.getTournament().getStage()) {
            case WAITING:
                if(evt.getEventName().equals("PlayerQuitEvent")) {
                    tournamentAPI.removeFromTournament(player);
                } else {
                    EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) evt;

                    if(isDead(ed)) {
                        ed.setCancelled(true);
                        ed.getEntity().teleport(tournamentAPI.getSpectatorArea());
                    }
                }
                break;
            case ACTIVE:

                for(Match match : tournamentAPI.getMatches()) {
                    if(match.toSet().contains(player)) {
                        if(evt.getEventName().equals("PlayerQuitEvent")) {
                            PlayerQuitEvent pq = (PlayerQuitEvent) evt;
                            player = pq.getPlayer();
                        } else {
                            EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) evt;
                            player = (Player) ed.getEntity();

                            if(!isDead(ed)) {
                                return;
                            }
                            ed.setCancelled(true);
                        }
                        match.setWinner( player == match.getInitiator() ? match.getOpponent() : match.getInitiator() );

                        tournamentAPI.removeFromTournament(player);
                        tournamentAPI.addWinner(match.getWinner());
                        tournamentAPI.endMatch(match);
                        break;
                    }
                }

                if(tournamentAPI.isInTournament(player)) {
                    tournamentAPI.removeFromTournament(player);
                }
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent evt) {
        if(plugin.getConfig().getBoolean("configuration.when-fighting.prevent-drop-items") && tournamentAPI.isInTournament(evt.getPlayer())) {
            evt.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent evt) {
        if(plugin.getConfig().getBoolean("configuration.disable-commands-in-tournament")) {
            if (!tournamentAPI.isTournamentRunning()) {
                return;
            }

            if (tournamentAPI.isInTournament(evt.getPlayer())) {
                Player player = evt.getPlayer();

                if(player.hasPermission("smarttournament.chatbypass")) {
                    return;
                }

                if (evt.getMessage().charAt(0) == '/') {
                    if (!plugin.getConfig().getStringList("configuration.cmd-whitelist").stream().anyMatch(s -> s.trim().equalsIgnoreCase(evt.getMessage().trim().split(" ")[0]))) {
                        player.sendMessage(Lang.COMMAND_USE_DENIED.toString());
                        evt.setCancelled(true);
                    }
                }

            }
        }
    }

    private boolean isDead(EntityDamageByEntityEvent evt) {
        return ((Player) evt.getEntity()).getHealth() <= evt.getFinalDamage();
    }
}
