package net.ihid.smarttournament;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Mikey on 5/8/2016.
 */
public class TournamentAPI {
    private final MainManager mainManager = TournamentPlugin.getMainManager();

    /** arena methods **/
    public boolean isOccupied(Arena arena) {
        return mainManager.getArenaManager().isOccupied(arena);
    }

    public Arena getAvailableArena() {
        return mainManager.getArenaManager().getAvailableArena();
    }

    public void setLocation(String arenaName, Player player, int num) {
        mainManager.getArenaManager().setLocation(arenaName, player, num);
    }

    public void loadArenas() {
        mainManager.getArenaManager().loadArenas();
    }

    /** match methods **/
    public List<Match> getMatches() {
        return mainManager.getMatchManager().getMatches();
    }

    public List<Player> getWinners() {
        return mainManager.getMatchManager().getWinners();
    }

    public void addWinner(Player player) {
        mainManager.getMatchManager().addWinner(player);
    }

    public void teleportPlayers(Match match) {
        mainManager.getMatchManager().teleportPlayers(match);
    }

    public void startMatch(Match match) {
        mainManager.getMatchManager().startMatch(match);
    }

    public void endMatch(Match match) {
        mainManager.getMatchManager().endMatch(match);
    }

    /** tournament methods **/
    public Tournament getTournament() {
        return mainManager.getTournamentManager().getTournament();
    }

    public List<Player> getPlayers() {
        return mainManager.getTournamentManager().getPlayers();
    }

    public boolean isInTournament(Player player) {
        return mainManager.getTournamentManager().isInTournament(player);
    }

    public void startTournament() {
        mainManager.getTournamentManager().startTournament();
    }

    public void endTournament() {
        mainManager.getTournamentManager().endTournament();
    }

    public boolean isTournamentRunning() {
        return mainManager.getTournamentManager().isTournamentRunning();
    }

    public Location getSpectatorArea() {
        return mainManager.getTournamentManager().getSpectatorArea();
    }

    public void setSpectatorArea(Player player) {
        mainManager.getTournamentManager().setSpectatorArea(player);
    }
}
