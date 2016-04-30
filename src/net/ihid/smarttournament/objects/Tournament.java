package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.enums.TournamentStage;
import net.ihid.smarttournament.tasks.PreTournamentTask;
import net.ihid.smarttournament.tasks.TournamentTask;
import org.bukkit.Bukkit;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Tournament {

    @Getter @Setter
    private TournamentStage stage;

    @Getter @Setter
    private int tournamentId;

    public Tournament() {
        this.stage = TournamentStage.WAITING;
    }

    public void start() {
        TournamentPlugin.getMainManager().getTournamentManager().addTournament(this);

        new PreTournamentTask(this).runTaskTimer(TournamentPlugin.i, 0L, 20L); // use enum way for config (look at phone bookmark)
        new TournamentTask(this).runTaskTimer(TournamentPlugin.i, 0L, 20L);
    }

    public void end() {
        if(Bukkit.getServer().getScheduler().isCurrentlyRunning(this.getTournamentId())) {
            Bukkit.getServer().getScheduler().cancelTask(this.getTournamentId());
        }

        TournamentPlugin.getMainManager().getTournamentManager().getTournaments()[0] = null;
    }
}
