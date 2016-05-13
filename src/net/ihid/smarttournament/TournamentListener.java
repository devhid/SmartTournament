package net.ihid.smarttournament;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentAPI;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    private Set<Player> deadPlayers;

    public TournamentListener(TournamentPlugin instance) {
        plugin = instance;
        deadPlayers = new HashSet<>();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent evt) {
        Player ps = evt.getPlayer();

        if(!api.isTournamentRunning()) {
            return;
        }

        if(deadPlayers.contains(ps)) {
            ps.teleport(api.getSpectatorArea());
            deadPlayers.remove(ps);
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent evt) {
        Player ps = evt.getEntity();

        if(!api.isTournamentRunning()) {
            return;
        }

        switch(api.getTournament().getStage()) {
            case WAITING:
                if(!api.isInTournament(ps)) {
                    return;
                }

                deadPlayers.add(ps);
                break;

            case ACTIVE:
                if(api.getPlayers().contains(ps)) {
                    api.getPlayers().remove(ps);
                }
                
                if(api.getPlayers().contains(ps)) {
                    api.getWinners().remove(ps);
                }

                for(Match match : api.getMatches()) {
                    if(match.toSet().contains(ps)) {
                        if (ps == match.getFirstPlayer()) {
                            match.setWinner(match.getSecondPlayer());
                        } else {
                            match.setWinner(match.getFirstPlayer());
                        }
                        
                        deadPlayers.add(ps);
                        
                        match.getArena().setOccupied(false);
                        match.setArena(null);
                        match.getWinner().teleport(api.getSpectatorArea());

                        api.addWinner(match.getWinner());
                        api.endMatch(match);
                        break;
                    }
                    break;
                }
                break;
            case FINISHED:
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        Player ps = evt.getPlayer();

        if(!api.isTournamentRunning()) {
            return;
        }

        switch(api.getTournament().getStage()) {
            case WAITING:
                if(!api.isInTournament(ps)) {
                    return;
                }
                api.getPlayers().remove(ps);
                break;

            case ACTIVE:
                if(api.getPlayers().contains(ps)) {
                    api.getPlayers().remove(ps);
                }
                
                if(api.getPlayers().contains(ps)) {
                    api.getWinners().remove(ps);
                }

                for(Match match : api.getMatches()) {
                    if(match.toSet().contains(ps)) {
                        if (ps == match.getFirstPlayer()) {
                            match.setWinner(match.getSecondPlayer());
                        } else {
                            match.setWinner(match.getFirstPlayer());
                        }
                        
                        match.getArena().setOccupied(false);
                        match.setArena(null);
                        match.toSet().forEach(player -> player.teleport(api.getSpectatorArea()));

                        api.addWinner(match.getWinner());
                        api.endMatch(match);
                        break;
                    }
                    break;
                }
                break;
            default:
                break;
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
