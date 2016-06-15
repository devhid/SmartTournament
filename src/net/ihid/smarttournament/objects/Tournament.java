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
        reset(false);
        TournamentPlugin.getTournamentAPI().loadArenas();

        new PreTournamentTask(this).runTaskTimer(TournamentPlugin.getInstance(), 0L, 20L);
    }

    public void end() {
        if(tournamentTask != null) {
            tournamentTask.cancel();
        }

        reset(true);
        setStage(TournamentStage.NON_ACTIVE);
    }

    public void reset(boolean includePlayers) {
        TournamentAPI api = TournamentPlugin.getTournamentAPI();

        if(includePlayers) {
            api.getPlayers().clear();
        }

        api.getWinners().clear();
        api.getMatches().clear();
        api.getArenas().clear();
    }
}
