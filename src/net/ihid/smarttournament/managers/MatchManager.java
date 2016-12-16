package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.api.events.MatchEndEvent;
import net.ihid.smarttournament.api.events.MatchStartEvent;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.player.NewPlayerState;
import net.ihid.smarttournament.player.SavedPlayerState;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.tasks.MatchTask;
import net.minelink.ctplus.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class MatchManager {
    private final TournamentPlugin plugin;
    private final MainManager mainManager;

    @Getter
    private final NewPlayerState newPlayerState;

    @Getter
    private final List<Match> matches;

    @Getter
    private final List<UUID> matchWinners;

    @Getter
    private final HashMap<String, SavedPlayerState> playerStates;

    public MatchManager(MainManager mainManager) {
        this.plugin = TournamentPlugin.getInstance();
        this.mainManager = mainManager;
        this.newPlayerState = new NewPlayerState();
        this.matches = new ArrayList<>();
        this.matchWinners = new ArrayList<>();
        this.playerStates = new HashMap<>();
    }

    public void addMatchWinner(Player player) {
        matchWinners.add(player.getUniqueId());
    }

    public void clearMatchWinners() {
        matchWinners.clear();
    }

    public void clearMatches() {
        matches.clear();
    }

    public boolean isInMatch(Player player) {
        return matches.stream().anyMatch(match -> match.toSet().contains(player));
    }

    public Match getMatch(Player player) {
        return matches.stream().filter(match -> match.toSet().contains(player)).findAny().orElse(null);
    }

    public void teleportPlayers(Match match) {
        removeTag(match.getInitiator(), match.getOpponent());

        match.getInitiator().teleport(match.getArena().getFirstLocation());
        match.getOpponent().teleport(match.getArena().getSecondLocation());
    }

    public void startMatch(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchStartEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_START_BROADCAST.toString().replace("{initiator}", match.getInitiator().getName()).replace("{opponent}", match.getOpponent().getName()));

        if(plugin.getConfig().getBoolean("configuration.hide-spectators-mode-enabled")) {
            if(TournamentPlugin.getHookHandler().getVanishNoPacketHook().isEnabled()) {
                match.toSet().forEach(player -> TournamentPlugin.getHookHandler().getVanishNoPacketHook().unvanish(player));
            }
        }

        teleportPlayers(match);
        matches.add(match);

        mapStates(playerStates, match);
        match.toSet().forEach(newPlayerState::modifyPlayer);

        MatchTask task = new MatchTask(match);
        match.setMatchTask(task);

        task.runTaskLater(TournamentPlugin.getInstance(), match.getDuration());
    }

    public Match getMatchById(int matchId) throws NullPointerException {
        return matches.stream().filter(match -> match.getMatchTask().getTaskId() == matchId).findAny().orElse(null);
    }

    public void endMatch(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchEndEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_WINNER_BROADCAST.toString().replace("{winner}", match.getWinner().getName()));

        //removeTag(match.getInitiator(), match.getOpponent());
        match.getWinner().teleport(mainManager.getSpectatorArea());

        matches.remove(match);
        unmapStates(playerStates, match);

        match.reset();
    }

    public void endMatchForcefully(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchEndEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_FORCE_END_BROADCAST.toString().replace("{initiator}", match.getInitiator().getName()).replace("{opponent}", match.getOpponent().getName()));

        //removeTag(match.getInitiator(), match.getOpponent());
        match.toSet().forEach(player -> player.teleport(mainManager.getWorldSpawn()));

        matches.remove(match);
        unmapStates(playerStates, match);

        match.reset();
        mainManager.removeFromTournament(match.getInitiator(), match.getOpponent());

        if(mainManager.getMatchWinners().size() == 0 && mainManager.getParticipants().size() < 1 && mainManager.getMatches().size() < 1) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_NO_WINNER_BROADCAST.toString());
            mainManager.getTournament().end();
        }

    }

    public void endIdleMatch(Match match) {
        plugin.getServer().getPluginManager().callEvent(new MatchEndEvent(match));
        Bukkit.broadcastMessage(Lang.MATCH_IDLE_BROADCAST.toString().replace("{initiator}", match.getInitiator().getName()).replace("{opponent}", match.getOpponent().getName()));

        //removeTag(match.getInitiator(), match.getOpponent());
        match.toSet().forEach(player -> player.teleport(mainManager.getWorldSpawn()));

        matches.remove(match);
        unmapStates(playerStates, match);

        match.reset();
        mainManager.removeFromTournament(match.getInitiator(), match.getOpponent());

        if(mainManager.getMatchWinners().size() == 0 && mainManager.getParticipants().size() < 1 && mainManager.getMatches().size() < 1) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_NO_WINNER_BROADCAST.toString());
            mainManager.getTournament().end();
        }
    }

    public void removeTag(Player... ps) {
        if (TournamentPlugin.getHookHandler().getCombatTagPlusHook().isEnabled()) {
            final TagManager tagManager = TournamentPlugin.getHookHandler().getCombatTagPlusHook().getTagManager();
            Arrays.stream(ps).filter(player -> player != null && tagManager.isTagged(player.getUniqueId())).forEach(player -> tagManager.untag(player.getUniqueId()));
        }
    }

    private void mapStates(HashMap<String, SavedPlayerState> states, Match match) {
        states.put(match.getInitiator().getName(), new SavedPlayerState(match.getInitiator()));
        states.put(match.getOpponent().getName(), new SavedPlayerState(match.getOpponent()));
    }

    private void unmapStates(HashMap<String, SavedPlayerState> states, Match match) {
        if(states.containsKey(match.getInitiator().getName())) {
            SavedPlayerState init = states.get(match.getInitiator().getName());
            init.revert();
            states.remove(match.getInitiator().getName());
        }

        if(states.containsKey(match.getOpponent().getName())) {
            SavedPlayerState op = states.get(match.getOpponent().getName());
            op.revert();
            states.remove(match.getOpponent().getName());
        }
    }
}
