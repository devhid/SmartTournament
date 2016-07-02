package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Mikey on 4/25/2016.
 */
public class MatchTask extends BukkitRunnable {
    private TournamentAPI tournamentAPI;
    private Match match;

    public MatchTask(Match match) {
        this.match = match;
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
    }

    public void run() {
        if(!match.isRunning()) {
            cancel();
            return;
        }

        Bukkit.broadcastMessage(Lang.MATCH_IDLE_BROADCAST.toString().replace("{initiator}", match.getInitiator().getName()).replace("{opponent}", match.getOpponent().getName()));
        tournamentAPI.removeFromTournament(match.getInitiator(), match.getOpponent());
        match.toSet().forEach(player -> player.teleport(tournamentAPI.getSpectatorArea()));
        cancel();
    }
}
