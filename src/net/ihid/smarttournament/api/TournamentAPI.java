package net.ihid.smarttournament.api;

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
     * Clears list containing arena elements.
     */
    public void clearArenas() {
        mainManager.getArenaManager().clearArenas();
    }

    /*
     * Checks if specified arena is occupied.
     *
     * @param arena - the arena to check for availability.
     *
     * @return true if arena is occupied, false if not.
     */
    public boolean isOccupied(Arena arena) {
        return mainManager.getArenaManager().isOccupied(arena);
    }

    /*
     * Returns arena that is not currently occupied.
     *
     * @return arena if available.
     */
    public Arena getAvailableArena() {
        return mainManager.getArenaManager().getAvailableArena();
    }

    /*
     * Adds an arena in the config file with the arena name, player's location, and position.
     *
     * @param arenaName - name of arena, player - player who is setting location, position - either 1 or 2.
     */
    public void setLocation(String arenaName, Player player, int position) {
        mainManager.getArenaManager().setLocation(arenaName, player, position);
    }

    /*
     * Returns list containing all arena elements.
     *
     * @return list of arena objects.
     */
    public List<Arena> getArenas() {
        return mainManager.getArenaManager().getArenas();
    }

    /*
     * Adds arena elements into list formed from locations defined in config file.
     */
    public void loadArenas() {
        mainManager.getArenaManager().loadArenas();
    }

    /** Match **/
    /*
     * Returns list containing all match elements.
     *
     * @return list of match objects.
     */
    public List<Match> getMatches() {
        return mainManager.getMatchManager().getMatches();
    }

    /*
     * Returns list containing current match winners.
     *
     * @return list of Player objects signifying match winners.
     */
    public List<Player> getWinners() {
        return mainManager.getMatchManager().getWinners();
    }

    /*
     * Clears list containing match elements.
     */
    public void clearMatches() {
        mainManager.getMatchManager().clearMatches();
    }

    /*
     * Clears list containing winner elements.
     */
    public void clearWinners() {
        mainManager.getMatchManager().clearWinners();
    }

    /*
     * Returns map of a player and their saved player state.
     *
     * @return map of player states.
     */
    public HashMap<Player, SavedPlayerState> getPlayerStates() {
        return mainManager.getMatchManager().getPlayerStates();
    }

    /*
     * Adds player to list containing winners.
     *
     * @param player who is being added as a match winner.
     */
    public void addWinner(Player player) {
        mainManager.getMatchManager().addWinner(player);
    }

    /*
     * Teleports players of a match to their locations inside the match's set arena.
     *
     * @param match - used to get its assigned arena.
     */
    public void teleportPlayers(Match match) {
        mainManager.getMatchManager().teleportPlayers(match);
    }

    /*
     * Starts a match.
     *
     * @param match - match that is being started.
     */
    public void startMatch(Match match) {
        mainManager.getMatchManager().startMatch(match);
    }

    /*
     * Ends a match.
     *
     * @param match - match that is being ended.
     */
    public void endMatch(Match match) {
        mainManager.getMatchManager().endMatch(match);
    }

    /*
     * Ends an idle match.
     *
     * @param match - match that is being ended for having idle participants.
     */
    public void endIdleMatch(Match match) {
        mainManager.getMatchManager().endIdleMatch(match);
    }

    /*
     * Removes combat tag of specified player(s).
     *
     * @param set containing player objects.
     */
    public void removeTag(Player... players) {
        mainManager.getMatchManager().removeTag(players);
    }

    /** Tournament **/
    /*
     * Returns tournament object.
     *
     * @return tournament object of type Tournament.
     */
    public Tournament getTournament() {
        return mainManager.getTournamentManager().getTournament();
    }

    /*
     * Returns list of players who have joined the tournament.
     *
     * @return list of player objects representing tournament participants.
     */
    public List<Player> getParticipants() {
        return mainManager.getTournamentManager().getParticipants();
    }

    /*
     * Clears list containing players who have joined the tournament.
     */
    public void clearParticipants() {
        mainManager.getTournamentManager().clearParticipants();
    }

    /*
     * Checks if a player is in the tournament.
     *
     * @param player who is checked to see if he or she is in tournament.
     *
     * @return true if player is in tournament, false if not.
     */
    public boolean isInTournament(Player player) {
        return mainManager.getTournamentManager().isInTournament(player);
    }

    /*
     * Adds player to list containing participants.
     *
     * @param player who is added as a participant.
     */
    public void addParticipant(Player player) {
        mainManager.getTournamentManager().addParticipant(player);
    }

    /*
     * Removes player from tournament.
     *
     * @param set of Player objects representing tournament participants.
     */
    public void removeFromTournament(Player... participants) {
        mainManager.getTournamentManager().removeFromTournament(participants);
    }

    /*
     * Starts a tournament.
     */
    public void startTournament() {
        mainManager.getTournamentManager().startTournament();
    }

    /*
     * Ends a tournament.
     */
    public void endTournament() {
        mainManager.getTournamentManager().endTournament();
    }

    /*
     * Checks if a tournament is currently running.
     *
     * @return true if tournament is running, false if not.
     */
    public boolean isTournamentRunning() {
        return mainManager.getTournamentManager().isTournamentRunning();
    }

    /*
     * Sets the world spawn inside the config file using player's location.
     *
     * @param player - uses its location to set world spawn.
     */
    public void setWorldSpawn(Player player) {
        mainManager.getTournamentManager().setWorldSpawn(player);
    }

    /*
     * Returns world spawn location using location defined inside the config file.
     *
     * @return location of world spawn.
     */
    public Location getWorldSpawn() {
        return mainManager.getTournamentManager().getWorldSpawn();
    }

    /*
     * Returns spectator area location using location defined inside the config file.
     *
     * @return location of spectator area.
     */
    public Location getSpectatorArea() {
        return mainManager.getTournamentManager().getSpectatorArea();
    }

    /*
     * Sets spectator area inside config using player's location.
     *
     * @param player - uses its location to set the spectator area.
     */
    public void setSpectatorArea(Player player) {
        mainManager.getTournamentManager().setSpectatorArea(player.getLocation());
    }

    /*
     * Sets spectator area inside config using player's location.
     *
     * @param location used to set the spectator area.
     */
    public void setSpectatorArea(Location location) {
        mainManager.getTournamentManager().setSpectatorArea(location);
    }

    /** Miscellaneous **/
    /*
     * Sets a player's hunger, exhaustion, health, gamemode, saturation, and potion effects to normal.
     *
     * @param player whose state is being affected.
     */
    public void setDefaultState(Player player) {
        mainManager.getMatchManager().getNewPlayerState().setDefaultState(player);
    }
}
