package net.ihid.smarttournament;

import com.jackproehl.plugins.CombatLog;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentAPI;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mikey on 4/26/2016.
 */
public class TournamentListener implements Listener {
    private TournamentPlugin plugin;

    private TournamentAPI api = TournamentPlugin.getTournamentAPI();

    private Set<String> deadPlayers; // eventually use config to get rid of players

    public TournamentListener(TournamentPlugin instance) {
        plugin = instance;
        deadPlayers = new HashSet<>();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        manageQuitOrDeath(evt, evt.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if(!(evt.getEntity() instanceof Player)) {
            return;
        }

        manageQuitOrDeath(evt, (Player) evt.getEntity());
    }

    private void manageQuitOrDeath(Event evt, Player ps) {
        if(!api.isTournamentRunning()) {
            return;
        }

        if(!api.isInTournament(ps)) {
            return;
        }

        switch(api.getTournament().getStage()) {
            case WAITING:
                if(evt.getEventName().equals("PlayerQuitEvent")) {
                    removeFromTournament(ps);
                } else {
                    EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) evt;

                    if(isDead(ed)) {
                        ed.getEntity().teleport(api.getSpectatorArea());
                    }
                }
                break;
            case ACTIVE:

                for(Match match : api.getMatches()) {
                    if(match.toSet().contains(ps)) {

                        if(evt.getEventName().equals("PlayerQuitEvent")) {
                            PlayerQuitEvent pq = (PlayerQuitEvent) evt;
                            ps = pq.getPlayer();
                        } else {
                            EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) evt;

                            ps = (Player) ed.getEntity();

                            if(!isDead(ed)) {
                                return;
                            }
                            ed.setCancelled(true);
                        }

                        if (ps == match.getInitiator()) {
                            match.setWinner(match.getOpponent());
                        } else {
                            match.setWinner(match.getInitiator());
                        }


                        ps.setHealth(20.0);

                        removeFromTournament(ps);

                        api.removeTag(ps);
                        api.removeTag(match.getWinner());

                        match.toSet().forEach(player -> player.teleport(api.getSpectatorArea()));

                        System.out.println("winners: " + api.getWinners().size());

                        api.addWinner(match.getWinner());
                        api.endMatch(match);
                        break;
                    }
                    break;
                }
                removeFromTournament(ps);
                break;
            default:
                break;
        }
    }

    private boolean isDead(EntityDamageByEntityEvent evt) {
        Player ps = (Player) evt.getEntity();

        return ps.getHealth() <= evt.getDamage();
    }

    private void removeFromTournament(Player ps) {
        if(api.getWinners().contains(ps)) {
            api.getWinners().remove(ps);
        }

        if(api.getPlayers().contains(ps)) {
            api.getPlayers().remove(ps);
        }
    }

    /*@EventHandler
    public void onCommand(PlayerCommandPreprocessEvent evt) {
        if(!api.isTournamentRunning()) {
            return;
        }

        if(api.isInTournament(evt.getPlayer())) {
            switch(evt.getMessage().) {
                case "leave":
                    break;
                case "join":
                    break;
                default:
                    evt.getPlayer().sendMessage("You cannot use any commands while in a tournament.");
                    evt.setCancelled(true);
            }
        }
    }*/
}
