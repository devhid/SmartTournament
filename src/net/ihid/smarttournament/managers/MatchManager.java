package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.TournamentAPI;
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
    private TournamentAPI api = TournamentPlugin.getTournamentAPI();

    private NewPlayerState nps = new NewPlayerState();

    @Getter
    private TagManager tm = TournamentPlugin.getCombatTag().getTagManager();

    @Getter
    private final List<Match> matches = new ArrayList<>();

    @Getter
    private final List<Player> winners = new ArrayList<>();

    @Getter
    private final HashMap<Player, SavedPlayerState> states = new HashMap<>();

    public void addWinner(Player player) {
        winners.add(player);
    }

    public void teleportPlayers(Match match) {
        removeTag(match.getInitiator(), match.getOpponent());

        match.getInitiator().teleport(match.getArena().getFirstLoc());
        match.getOpponent().teleport(match.getArena().getSecondLoc());
    }

    public void startMatch(Match match) {
        Bukkit.broadcastMessage(ChatUtil.color("&4Tournament &8// &c" + match.getInitiator().getName() + " &7and&c " + match.getOpponent().getName() + " &7are now fighting."));

        teleportPlayers(match);
        matches.add(match);

        mapStates(states, match);
        match.toSet().forEach(player -> nps.modifyPlayer(player));

        MatchTask task = new MatchTask(match);
        match.setMatchTask(task);

        task.runTaskLater(TournamentPlugin.getInstance(), match.getDuration());
    }

    public void endMatch(Match match) {
        Bukkit.broadcastMessage(ChatUtil.color("&4Tournament &8// &c" + match.getWinner().getName() + " &7has won the fight!"));

        removeTag(match.getInitiator(), match.getOpponent());
        match.toSet().forEach(player -> player.teleport(api.getSpectatorArea()));

        matches.remove(match);
        unmapStates(states, match);

        match.reset();
    }

    public void removeTag(Player... ps) {
        Arrays.stream(ps).filter(player -> player != null && tm.isTagged(player.getUniqueId())).forEach(player -> tm.untag(player.getUniqueId()));
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
