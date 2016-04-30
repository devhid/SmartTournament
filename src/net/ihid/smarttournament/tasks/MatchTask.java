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
        match.setMatchId(getTaskId());
    }

    public void run() {
        Bukkit.broadcastMessage("&a" + match.getFirstPlayer() + "&7 and &a" + match.getSecondPlayer() + "&7 have both been eliminated for idling.");

        TournamentPlugin.getMainManager().getMatchManager().endMatch(match);
        match.toSet().forEach(player -> player.teleport(TournamentPlugin.getMainManager().getTournamentManager().getSpectatorArea()));
        cancel();
    }
}
