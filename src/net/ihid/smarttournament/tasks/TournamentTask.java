package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.enums.TournamentStage;
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
    private Tournament tournament;
    private List<Player> winners, players;

    public TournamentTask(Tournament tournament) {
        this.tournament = tournament;
        tournament.setStage(TournamentStage.ACTIVE);

        winners = TournamentPlugin.getMainManager().getMatchManager().getWinners();
        players = TournamentPlugin.getMainManager().getTournamentManager().getPlayers();
    }

    public void run() {
        Arena arena = TournamentPlugin.getMainManager().getArenaManager().getAvailableArena();

        if(arena == null) {
            return;
        }

        if(players.size() > 1) {
            Match match = new Match(players.remove(0), players.remove(0));
            match.setArena(arena);

            arena.setOccupied(true);
            TournamentPlugin.getMainManager().getMatchManager().startMatch(match);
        }

        else if(players.size() == 1) {
            winners.add(players.remove(0));
        }

        else if(winners.size() > 1) {
            players.addAll(winners);
            winners.clear();
        }

        else if(winners.size() == 1 && TournamentPlugin.getMainManager().getMatchManager().getMatches().size() == 0) {
            Bukkit.broadcastMessage("&e" + winners.get(0).getName() + " has won the tournament!");
            tournament.setStage(TournamentStage.FINISHED);
            cancel();
        }

        else if(TournamentPlugin.getMainManager().getMatchManager().getMatches().size() == 0) {
            Bukkit.broadcastMessage("&cThe tournament has ended due to an error.");
            cancel();
        }
    }
}
