package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentAPI;
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
    private TournamentAPI api = TournamentPlugin.getTournamentAPI();

    private Tournament tournament;

    private List<Player> winners, players;

    public TournamentTask(Tournament tournament) {
        this.tournament = tournament;
        tournament.setStage(TournamentStage.ACTIVE);

        winners = api.getWinners();
        players = api.getPlayers();
    }

    public void run() {
        Arena arena = api.getAvailableArena();

        if(arena == null) {
            return;
        }

        if(players.size() > 1) {
            Match match = new Match(players.remove(0), players.remove(0));
            match.setArena(arena);

            arena.setOccupied(true);
            api.startMatch(match);
        }

        else if(players.size() == 1) {
            winners.add(players.remove(0));
        }

        else if(winners.size() > 1) {
            players.addAll(winners);
            winners.clear();
        }

        else if(winners.size() == 1 && api.getMatches().size() == 0) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_WINNER_BROADCAST.toString().replace("{winner}", winners.get(0).getName()));
            tournament.end();
        }

        else if(api.getMatches().size() == 0) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_END_ERROR.toString());
            tournament.end();
        }
    }
}
