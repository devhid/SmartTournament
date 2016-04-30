package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Mikey on 4/25/2016.
 */
public class PreTournamentTask extends BukkitRunnable {
    private Tournament tournament;
    private int countdown;

    public PreTournamentTask(Tournament tournament) {
        this.tournament = tournament;
        this.countdown = 120;
    }

    public void run() {
        if(countdown == 0) {
            Bukkit.broadcastMessage("&eThe tournament has started!");
            cancel();
        }

        else if(countdown == 120 || countdown == 60 || countdown == 30 || countdown == 10 || countdown == 3 || countdown == 2 || countdown == 1) {
            Bukkit.broadcastMessage("&7The tournament will begin in &c " + countdown + " &7second(s).");
        }

        countdown--;
    }
}
