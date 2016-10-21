package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchTask extends BukkitRunnable {
    private final Match match;

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
