package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.tasks.MatchTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.util.*;

/**
 * Created by Mikey on 4/24/2016.
 */
public class MatchManager {
    @Getter
    private final Set<Match> matches = new TreeSet<>();

    private final List<Player> winners = new ArrayList<>();

    public List<Player> getWinners() {
        matches.forEach(match -> winners.add(match.getWinner()));
        return winners;
    }

    public void addWinner(Player player) {
        winners.add(player);
    }

    public void teleportPlayers(Match match) {
        match.getFirstPlayer().teleport(match.getArena().getFirstLoc());
        match.getSecondPlayer().teleport(match.getArena().getSecondLoc());
    }

    public void startMatch(Match match) {
        teleportPlayers(match);
        matches.add(match);
        new MatchTask(match).runTaskLaterAsynchronously(TournamentPlugin.i, match.getDuration());
    }

    public void endMatch(Match match) {
        if(Bukkit.getServer().getScheduler().isCurrentlyRunning(match.getMatchId())) {
            Bukkit.getServer().getScheduler().cancelTask(match.getMatchId());
        }
        matches.remove(match);
    }
}
