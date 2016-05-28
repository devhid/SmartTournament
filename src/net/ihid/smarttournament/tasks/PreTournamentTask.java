package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
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
        this.countdown = 10;
        tournament.setStage(TournamentStage.WAITING);
    }

    public void run() {
        if(countdown == 0) {
            if(TournamentPlugin.getTournamentAPI().getPlayers().size() < 2) {
                tournament.end();
                Bukkit.broadcastMessage(ChatUtil.color("&4Tournament &8// &eThere are not enough players for this tournament to start."));
            } else {
                Bukkit.broadcastMessage(ChatUtil.color("&4Tournament &8// &eThe tournament has started!"));
                TournamentTask task = new TournamentTask(tournament);
                tournament.setTournamentTask(task);

                task.runTaskTimer(TournamentPlugin.i, 0L, 20L);
            }
            cancel();
        }

        else if(countdown == 120 || countdown == 60 || countdown == 30 || countdown == 10 || countdown == 3 || countdown == 2 || countdown == 1) {
            Bukkit.broadcastMessage(ChatUtil.color("&4Tournament &8// &7The tournament will begin in&c " + countdown + " &7second(s)."));
        }

        countdown--;
    }
}
