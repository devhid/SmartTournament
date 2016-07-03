package net.ihid.smarttournament;

import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.objects.Tournament;
import net.ihid.smarttournament.player.SavedPlayerState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class TournamentAPI {
    private MainManager mainManager;

    public TournamentAPI() {
        this.mainManager = new MainManager();
    }

    /** Arena **/
    /*
        Clears list containing arena elements.
     */
    public void clearArenas() {
        mainManager.getArenaManager().clearArenas();
    }

    /*
        Checks if specified arena is occupied.
     */
    public boolean isOccupied(Arena arena) {
        return mainManager.getArenaManager().isOccupied(arena);
    }

    /*
        Returns arena that is not currently occupied.
     */
    public Arena getAvailableArena() {
        return mainManager.getArenaManager().getAvailableArena();
    }

    /*
        Adds an arena in the config file with the arena name, player's location, and position.
     */
    public void setLocation(String arenaName, Player player, int position) {
        mainManager.getArenaManager().setLocation(arenaName, player, position);
    }

    /*
        Returns list containing all arena elements.
     */
    public List<Arena> getArenas() {
        return mainManager.getArenaManager().getArenas();
    }

    /*
        Adds arena elements into list formed from locations defined in config file.
     */
    public void loadArenas() {
        mainManager.getArenaManager().loadArenas();
    }

    /** Match **/
    /*
        Returns list containing all match elements.
     */
    public List<Match> getMatches() {
        return mainManager.getMatchManager().getMatches();
    }

    /*
        Returns list containing current match winners.
     */
    public List<Player> getWinners() {
        return mainManager.getMatchManager().getWinners();
    }

    /*
        Clears list containing match elements.
     */
    public void clearMatches() {
        mainManager.getMatchManager().clearMatches();
    }

    /*
        Clears list containing winner elements.
     */
    public void clearWinners() {
        mainManager.getMatchManager().clearWinners();
    }

    /*
        Returns map of a player and their saved player state.
     */
    public HashMap<Player, SavedPlayerState> getPlayerStates() {
        return mainManager.getMatchManager().getPlayerStates();
    }

    /*
        Adds player to list containing winners.
     */
    public void addWinner(Player player) {
        mainManager.getMatchManager().addWinner(player);
    }

    /*
        Teleports players of a match to their locations inside the match's set arena.
     */
    public void teleportPlayers(Match match) {
        mainManager.getMatchManager().teleportPlayers(match);
    }

    /*
        Starts a match.
     */
    public void startMatch(Match match) {
        mainManager.getMatchManager().startMatch(match);
    }

    /*
        Ends a match.
     */
    public void endMatch(Match match) {
        mainManager.getMatchManager().endMatch(match);
    }

    /*
        Ends an idle match.
     */
    public void endIdleMatch(Match match) {
        mainManager.getMatchManager().endIdleMatch(match);
    }

    /*
        Removes combat tag of specified player(s).
     */
    public void removeTag(Player... ps) {
        mainManager.getMatchManager().removeTag(ps);
    }

    /** Tournament **/
    /*
        Returns tournament object.
     */
    public Tournament getTournament() {
        return mainManager.getTournamentManager().getTournament();
    }

    /*
        Returns list of players who have joined the tournament.
     */
    public List<Player> getParticipants() {
        return mainManager.getTournamentManager().getParticipants();
    }

    /*
        Clears list containing players who have joined the tournament.
     */
    public void clearParticipants() {
        mainManager.getTournamentManager().clearParticipants();
    }

    /*
        Checks if a player is in the tournament.
     */
    public boolean isInTournament(Player player) {
        return mainManager.getTournamentManager().isInTournament(player);
    }

    /*
        Adds player to list containing participants.
     */
    public void addParticipant(Player player) {
        mainManager.getTournamentManager().addParticipant(player);
    }

    /*
        Removes player from tournament.
     */
    public void removeFromTournament(Player... participants) {
        mainManager.getTournamentManager().removeFromTournament(participants);
    }

    /*
        Starts a tournament.
     */
    public void startTournament() {
        mainManager.getTournamentManager().startTournament();
    }

    /*
        Ends a tournament.
     */
    public void endTournament() {
        mainManager.getTournamentManager().endTournament();
    }

    /*
        Checks if a tournament is currently running.
     */
    public boolean isTournamentRunning() {
        return mainManager.getTournamentManager().isTournamentRunning();
    }

    /*
        Sets the world spawn inside the config file using player's location.
     */
    public void setWorldSpawn(Player player) {
        mainManager.getTournamentManager().setWorldSpawn(player);
    }

    /*
        Returns world spawn location using location defined inside the config file.
     */
    public Location getWorldSpawn() {
        return mainManager.getTournamentManager().getWorldSpawn();
    }

    /*
        Returns spectator area location using location defined inside the config file.
     */
    public Location getSpectatorArea() {
        return mainManager.getTournamentManager().getSpectatorArea();
    }

    /*
        Sets spectator area inside config using player's location.
     */
    public void setSpectatorArea(Player player) {
        mainManager.getTournamentManager().setSpectatorArea(player);
    }

    /** Miscellaneous **/
    /*
        Sets a player's hunger, exhaustion, health, gamemode, saturation, and potion effects to normal.
     */
    public void setDefaultState(Player player) {
        mainManager.getMatchManager().getNewPlayerState().setDefaultState(player);
    }

    public String toString() {
        return "TournamentAPI is not null.";
    }
}
