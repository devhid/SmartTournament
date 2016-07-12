package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by Mikey on 4/25/2016.
 */
public class TournamentTask extends BukkitRunnable {
    private TournamentAPI tournamentAPI;
    private Tournament tournament;
    private List<Player> winners, participants;

    public TournamentTask(Tournament tournament) {
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
        this.tournament = tournament;
        this.winners = tournamentAPI.getWinners();
        this.participants = tournamentAPI.getParticipants();
        tournament.setStage(TournamentStage.ACTIVE);
    }

    public void run() {
        Arena arena = tournamentAPI.getAvailableArena();

        if(arena == null) {
            return;
        }

        if(participants.size() > 1) {
            Match match = new Match(participants.remove(0), participants.remove(0));
            match.setArena(arena);

            arena.setOccupied(true);
            tournamentAPI.startMatch(match);
        }

        else if(participants.size() == 1) {
            winners.add(participants.remove(0));
        }

        else if(winners.size() > 1) {
            participants.addAll(winners);
            winners.clear();
        }

        else if(winners.size() == 1 && tournamentAPI.getMatches().size() == 0) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_WINNER_BROADCAST.toString().replace("{winner}", winners.get(0).getName()));
            tournament.end();
        }
    }
}
