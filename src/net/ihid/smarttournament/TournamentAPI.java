package net.ihid.smarttournament;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.objects.Tournament;
import net.ihid.smarttournament.player.SavedPlayerState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mikey on 5/8/2016.
 */
public class TournamentAPI {
    private final MainManager mainManager = new MainManager();

    /** Arena **/
    public void clearArenas() {
        mainManager.getArenaManager().clearArenas();
    }

    public boolean isOccupied(Arena arena) {
        return mainManager.getArenaManager().isOccupied(arena);
    }

    public Arena getAvailableArena() {
        return mainManager.getArenaManager().getAvailableArena();
    }

    public void setLocation(String arenaName, Player player, int num) {
        mainManager.getArenaManager().setLocation(arenaName, player, num);
    }

    public List<Arena> getArenas() {
        return mainManager.getArenaManager().getArenas();
    }

    public void loadArenas() {
        mainManager.getArenaManager().loadArenas();
    }

    /** Match **/
    public List<Match> getMatches() {
        return mainManager.getMatchManager().getMatches();
    }

    public List<Player> getWinners() {
        return mainManager.getMatchManager().getWinners();
    }

    public void clearMatches() {
        mainManager.getMatchManager().clearMatches();
    }

    public void clearWinners() {
        mainManager.getMatchManager().clearWinners();
    }

    public HashMap<Player, SavedPlayerState> getPlayerStates() {
        return mainManager.getMatchManager().getPlayerStates();
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

    public void removeTag(Player... ps) {
        mainManager.getMatchManager().removeTag(ps);
    }

    /** Tournament **/
    public Tournament getTournament() {
        return mainManager.getTournamentManager().getTournament();
    }

    public List<Player> getParticipants() {
        return mainManager.getTournamentManager().getParticipants();
    }

    public void clearParticipants() {
        mainManager.getTournamentManager().clearParticipants();
    }

    public boolean isInTournament(Player player) {
        return mainManager.getTournamentManager().isInTournament(player);
    }

    public void addToTournament(Player player) {
        mainManager.getTournamentManager().addToTournament(player);
    }

    public void removeFromTournament(Player player) {
        mainManager.getTournamentManager().removeFromTournament(player);
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

    public void setWorldSpawn(Player player) {
        mainManager.getTournamentManager().setWorldSpawn(player);
    }

    public Location getWorldSpawn() {
        return mainManager.getTournamentManager().getWorldSpawn();
    }

    public Location getSpectatorArea() {
        return mainManager.getTournamentManager().getSpectatorArea();
    }

    public void setSpectatorArea(Player player) {
        mainManager.getTournamentManager().setSpectatorArea(player);
    }

    /** Miscellaneous **/
    public void setDefaultState(Player player) {
        mainManager.getMatchManager().getNewPlayerState().setDefaultState(player);
    }
}
