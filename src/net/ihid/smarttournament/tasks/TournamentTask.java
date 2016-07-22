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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Mikey on 4/25/2016.
 */
public class TournamentTask extends BukkitRunnable {
    private TournamentAPI tournamentAPI;
    private Tournament tournament;
    private List<UUID> matchWinners, participants;

    public TournamentTask(Tournament tournament) {
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
        this.tournament = tournament;
        this.matchWinners = tournamentAPI.getMatchWinners();
        this.participants = tournamentAPI.getParticipants();
        tournament.setStage(TournamentStage.ACTIVE);
    }

    public void run() {
        if(tournamentAPI.getAvailableArena() == null) {
            return;
        }

        Arena arena = tournamentAPI.getAvailableArena();

        if(participants.size() > 1) {
            Match match = new Match(Bukkit.getPlayer(participants.remove(0)), Bukkit.getPlayer(participants.remove(0)));
            match.setArena(arena);

            arena.setOccupied(true);
            tournamentAPI.startMatch(match);
            /*Bukkit.broadcastMessage("Starting new match, participant size: " + participants.size());
            Bukkit.broadcastMessage("Starting new match, winner size: " + matchWinners.size());*/

        }

        else if(participants.size() == 1) {
            matchWinners.add(participants.remove(0));
            /*Bukkit.broadcastMessage("Check for part. size == 1, participant size: " + participants.size());
            Bukkit.broadcastMessage("Check for part. size == 1, winner size: " + matchWinners.size());*/

        }

        else if(matchWinners.size() > 1 && participants.size() < 1) {
            participants.addAll(matchWinners);
            matchWinners.clear();
            /*Bukkit.broadcastMessage("Check for win. size > 1, participant size: " + participants.size());
            Bukkit.broadcastMessage("Check for win. size > 1, winner size: " + matchWinners.size());*/
        }

        else if(matchWinners.size() == 1 && participants.size() < 1) {
            /*Bukkit.broadcastMessage("Check for winner, participant size: " + participants.size());
            Bukkit.broadcastMessage("Check for winner, winner size: " + matchWinners.size());*/
            Bukkit.broadcastMessage(Lang.TOURNAMENT_WINNER_BROADCAST.toString().replace("{winner}", Bukkit.getPlayer(matchWinners.get(0)).getName()));
            tournament.end();
        }
    }
}
