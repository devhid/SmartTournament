package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.tasks.PreTournamentTask;
import net.ihid.smarttournament.tasks.TournamentTask;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Tournament {

    @Getter @Setter
    private TournamentStage stage;

    @Getter @Setter
    private TournamentTask tournamentTask;

    public Tournament() {
        this.stage = TournamentStage.NON_ACTIVE;
    }

    public void start() {
        TournamentPlugin.getTournamentAPI().getArenas().clear();
        TournamentPlugin.getTournamentAPI().loadArenas();

        new PreTournamentTask(this).runTaskTimer(TournamentPlugin.i, 0L, 20L); // use enum way for config (look at phone bookmark)
    }

    public void end() {
        if(tournamentTask != null) {
            tournamentTask.cancel();
        }

        reset();
        setStage(TournamentStage.NON_ACTIVE);
    }

    public void reset() {
        TournamentAPI api = TournamentPlugin.getTournamentAPI();

        api.getPlayers().clear();
        api.getWinners().clear();
        api.getMatches().clear();
        api.getArenas().clear();
    }
}
