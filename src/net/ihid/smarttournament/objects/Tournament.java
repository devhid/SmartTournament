package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.enums.TournamentStage;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.managers.TournamentManager;
import net.ihid.smarttournament.tasks.PreTournamentTask;
import net.ihid.smarttournament.tasks.TournamentTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Tournament {

    @Getter @Setter
    private TournamentStage stage;

    @Getter @Setter
    private int tournamentId;

    public Tournament() {
        this.stage = TournamentStage.NON_ACTIVE;
    }

    public void start() {
        TournamentPlugin.getTournamentAPI().loadArenas();

        new PreTournamentTask(this).runTaskTimer(TournamentPlugin.i, 0L, 20L); // use enum way for config (look at phone bookmark)
    }

    public void end() {
        if(Bukkit.getServer().getScheduler().isCurrentlyRunning(this.getTournamentId())) {
            Bukkit.getServer().getScheduler().cancelTask(this.getTournamentId());
        }
    }

    /*public void manage(List<Player> players) {
        Arena arena = TournamentPlugin.getMainManager().getArenaManager().getAvailableArena();

        if(arena == null) {
            return;
        }

        if(players.size() > 1) {
            Match match = new Match(players.remove(0), players.remove(0));
            match.setArena(arena);

            arena.setOccupied(true);
            TournamentPlugin.getMainManager().getMatchManager().startMatch(match);
            manage(players);
        }

        if(players.size() == 1) {
            TournamentPlugin.getMainManager().getMatchManager().addWinner(players.remove(0));
            manage(players);
        }
    }*/
}
