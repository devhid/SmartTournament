package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mikey on 4/26/2016.
 */
public class TournamentListener implements Listener {
    private TournamentPlugin plugin;

    private static final MainManager mainManager = TournamentPlugin.getMainManager();

    private Set<Player> deadPlayers;

    public TournamentListener(TournamentPlugin instance) {
        plugin = instance;
        deadPlayers = new HashSet<>();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent evt) {
        Player ps = evt.getPlayer();

        if(!mainManager.getTournamentManager().isTournamentRunning()) {
            return;
        }

        if(deadPlayers.contains(ps)) {
            ps.teleport(mainManager.getTournamentManager().getSpectatorArea());
            deadPlayers.remove(ps);
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent evt) {
        Player ps = evt.getEntity();

        if(!mainManager.getTournamentManager().isTournamentRunning()) {
            return;
        }

        switch(mainManager.getTournamentManager().getTournament().getStage()) {
            case WAITING:
                if(!mainManager.getTournamentManager().isInTournament(ps)) {
                    return;
                }

                deadPlayers.add(ps);
                break;

            case ACTIVE:
                mainManager.getTournamentManager().getPlayers().remove(ps);
                mainManager.getMatchManager().getWinners().remove(ps);

                for(Match match : mainManager.getMatchManager().getMatches()) {
                    if(match.toSet().contains(ps)) {
                        if (ps == match.getFirstPlayer()) {
                            match.setWinner(match.getSecondPlayer());
                        } else {
                            match.setWinner(match.getFirstPlayer());
                        }

                        deadPlayers.add(ps);
                        mainManager.getMatchManager().addWinner(match.getWinner());
                        match.getWinner().teleport(mainManager.getTournamentManager().getSpectatorArea());

                        mainManager.getMatchManager().endMatch(match);
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

        if(!mainManager.getTournamentManager().isTournamentRunning()) {
            return;
        }

        switch(mainManager.getTournamentManager().getTournament().getStage()) {
            case WAITING:
                if(!mainManager.getTournamentManager().isInTournament(ps)) {
                    return;
                }
                mainManager.getTournamentManager().getPlayers().remove(ps);
                break;

            case ACTIVE:
                mainManager.getTournamentManager().getPlayers().remove(ps);
                mainManager.getMatchManager().getWinners().remove(ps);

                for(Match match : mainManager.getMatchManager().getMatches()) {
                    if(match.toSet().contains(ps)) {
                        if (ps == match.getFirstPlayer()) {
                            match.setWinner(match.getSecondPlayer());
                        } else {
                            match.setWinner(match.getFirstPlayer());
                        }
                        match.toSet().forEach(player -> player.teleport(mainManager.getTournamentManager().getSpectatorArea()));

                        mainManager.getMatchManager().addWinner(match.getWinner());
                        mainManager.getMatchManager().endMatch(match);
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

    /*@EventHandler
    public void onCommand(PlayerCommandPreprocessEvent evt) {
        if(!mainManager.getTournamentManager().isTournamentRunning()) {
            return;
        }

        if(mainManager.getTournamentManager().isInTournament(evt.getPlayer())) {
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
