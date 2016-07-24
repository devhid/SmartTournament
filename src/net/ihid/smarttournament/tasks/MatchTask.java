package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.objects.Match;
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

        TournamentPlugin.getMainManager().endIdleMatch(match);
        cancel();
    }
}
