package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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
        if(TournamentPlugin.getMainManager().getParticipants().size() == plugin.getConfig().getInt("configuration.start-when-max-players-reached")) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_POST_START_BROADCAST.toString());
            TournamentTask task = new TournamentTask(tournament);
            tournament.setTournamentTask(task);

            task.runTaskTimer(TournamentPlugin.getInstance(), 0L, 40L);
            cancel();
        }

        if(countdown == 0) {
            if(TournamentPlugin.getMainManager().getParticipants().size() < plugin.getConfig().getInt("configuration.minimum-players-to-start")) {
                tournament.end();
                Bukkit.broadcastMessage(Lang.NOT_ENOUGH_PLAYERS.toString());
            } else {
                Bukkit.broadcastMessage(Lang.TOURNAMENT_POST_START_BROADCAST.toString());
                TournamentTask task = new TournamentTask(tournament);
                tournament.setTournamentTask(task);

                task.runTaskTimer(TournamentPlugin.getInstance(), 0L, 40L);
            }
            cancel();
        }

        else if(plugin.getConfig().getIntegerList("configuration.countdown-values").contains(countdown)) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_COUNTDOWN_BROADCAST.toString().replace("{countdown}", String.valueOf(countdown)));
        }

        countdown--;
    }
}
