package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Mikey on 4/25/2016.
 */
public class MatchTask extends BukkitRunnable {
    private Match match;

    public MatchTask(Match match) {
        this.match = match;
    }

    public void run() {
        if(!match.isRunning()) {
            cancel();
            return;
        }

        Bukkit.broadcastMessage("&a" + match.getInitiator().getName() + "&7 and &a" + match.getOpponent().getName() + "&7 have both been eliminated for idling.");
        TournamentPlugin.getTournamentAPI().endMatch(match);

        match.toSet().forEach(player -> player.teleport(TournamentPlugin.getTournamentAPI().getSpectatorArea()));
        cancel();
    }
}
