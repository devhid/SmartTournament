package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.TournamentAPI;
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

/**
 * Created by Mikey on 4/24/2016.
 */
public class MatchManager {
    private TagManager tagManager;

    @Getter
    private final NewPlayerState newPlayerState;

    @Getter
    private final List<Match> matches;

    @Getter
    private final List<Player> winners;

    @Getter
    private final HashMap<Player, SavedPlayerState> playerStates;

    public MatchManager() {
        if(TournamentPlugin.getCombatTag() != null) {
            tagManager = TournamentPlugin.getCombatTag().getTagManager();
        }
        this.newPlayerState = new NewPlayerState();
        this.matches = new ArrayList<>();
        this.winners = new ArrayList<>();
        this.playerStates = new HashMap<>();
    }

    public void addWinner(Player player) {
        winners.add(player);
    }

    public void clearWinners() {
        winners.clear();
    }

    public void clearMatches() {
        matches.clear();
    }

    public void teleportPlayers(Match match) {
        removeTag(match.getInitiator(), match.getOpponent());

        match.getInitiator().teleport(match.getArena().getFirstLoc());
        match.getOpponent().teleport(match.getArena().getSecondLoc());
    }

    public void startMatch(Match match) {
        Bukkit.broadcastMessage(Lang.MATCH_START_BROADCAST.toString()
                .replace("{initiator}", match.getInitiator().getName())
                .replace("{opponent}", match.getOpponent().getName()));

        teleportPlayers(match);
        matches.add(match);

        mapStates(playerStates, match);
        match.toSet().forEach(newPlayerState::modifyPlayer);

        MatchTask task = new MatchTask(match);
        match.setMatchTask(task);

        task.runTaskLater(TournamentPlugin.getInstance(), match.getDuration());
    }

    public void endMatch(Match match) {
        TournamentAPI api = TournamentPlugin.getTournamentAPI();
        Bukkit.broadcastMessage(Lang.MATCH_WINNER_BROADCAST.toString().replace("{winner}", match.getWinner().getName()));

        removeTag(match.getInitiator(), match.getOpponent());
        match.toSet().forEach(player -> player.teleport(api.getSpectatorArea()));

        matches.remove(match);
        unmapStates(playerStates, match);

        match.reset();
    }

    public void removeTag(Player... ps) {
        if(TournamentPlugin.getCombatTag() != null) {
            Arrays.stream(ps).filter(player -> player != null && tagManager.isTagged(player.getUniqueId())).forEach(player -> tagManager.untag(player.getUniqueId()));
        }
    }

    private void mapStates(HashMap<Player, SavedPlayerState> states, Match match) {
        states.put(match.getInitiator(), new SavedPlayerState(match.getInitiator()));
        states.put(match.getOpponent(), new SavedPlayerState(match.getOpponent()));
    }

    private void unmapStates(HashMap<Player, SavedPlayerState> states, Match match) {
        SavedPlayerState init = states.get(match.getInitiator());
        init.revert();

        SavedPlayerState op = states.get(match.getOpponent());
        op.revert();

        states.remove(match.getInitiator());
        states.remove(match.getOpponent());
    }
}
