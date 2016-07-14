package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Mikey on 4/25/2016.
 */
public class PreTournamentTask extends BukkitRunnable {
    private TournamentPlugin plugin;
    private Tournament tournament;
    private int countdown;

    public PreTournamentTask(Tournament tournament) {
        this.plugin = TournamentPlugin.getInstance();
        this.tournament = tournament;
        this.countdown = plugin.getConfig().getInt("configuration.tournament-start-delay");
        tournament.setStage(TournamentStage.WAITING);
    }

    public void run() {
        if(countdown == 0) {
            if(TournamentPlugin.getTournamentAPI().getParticipants().size() < plugin.getConfig().getInt("configuration.minimum-players-to-start")) { // configure minimum starting players
                tournament.end();
                Bukkit.broadcastMessage(Lang.NOT_ENOUGH_PLAYERS.toString());
            } else {
                Bukkit.broadcastMessage(Lang.TOURNAMENT_POST_START_BROADCAST.toString());
                TournamentTask task = new TournamentTask(tournament);
                tournament.setTournamentTask(task);

                task.runTaskTimer(TournamentPlugin.getInstance(), 0L, 20L);
            }
            cancel();
        }

        else if(plugin.getConfig().getIntegerList("configuration.countdown-values").contains(countdown)) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_COUNTDOWN_BROADCAST.toString().replace("{countdown}", String.valueOf(countdown)));
        }

        countdown--;
    }
}
