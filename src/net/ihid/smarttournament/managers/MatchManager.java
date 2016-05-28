package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.tasks.MatchTask;
import net.minelink.ctplus.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;


import java.util.*;

/**
 * Created by Mikey on 4/24/2016.
 */
public class MatchManager {
    @Getter
    private final List<Match> matches = new ArrayList<>();

    @Getter
    private final List<Player> winners = new ArrayList<>();

    public void addWinner(Player player) {
        winners.add(player);
    }

    public void teleportPlayers(Match match) {
        final TagManager tagManager = TournamentPlugin.getCombatTag().getTagManager();
        match.toSet().stream().filter(player -> player != null && tagManager.isTagged(player.getUniqueId())).forEach(player -> tagManager.untag(player.getUniqueId()));

        match.getInitiator().teleport(match.getArena().getFirstLoc());
        match.getOpponent().teleport(match.getArena().getSecondLoc());
    }

    public void startMatch(Match match) {
        Bukkit.broadcastMessage(ChatUtil.color("&4Tournament &8// &c" + match.getInitiator().getName() + " &7and&c " + match.getOpponent().getName() + " &7are now fighting."));
        System.out.println("start match: " + TournamentPlugin.getTournamentAPI().getPlayers().size());

        teleportPlayers(match);
        matches.add(match);

        MatchTask task = new MatchTask(match);
        match.setMatchTask(task);

        task.runTaskLater(TournamentPlugin.i, match.getDuration());
    }

    public void endMatch(Match match) {
        Bukkit.broadcastMessage(ChatUtil.color("&4Tournament &8// &c" + match.getWinner().getName() + " &7has won the fight!"));

        final TagManager tagManager = TournamentPlugin.getCombatTag().getTagManager();
        match.toSet().stream().filter(player -> player != null && tagManager.isTagged(player.getUniqueId())).forEach(player -> tagManager.untag(player.getUniqueId()));

        match.reset();
        matches.remove(match);
    }

    public void removeTag(Player... ps) {
        final TagManager tm = TournamentPlugin.getCombatTag().getTagManager();
        Arrays.stream(ps).filter(player -> tm.isTagged(player.getUniqueId())).forEach(player -> tm.untag(player.getUniqueId()));

        /*if(TournamentPlugin.getCombatTag().getTagManager().isTagged(ps.getUniqueId())) {
            TournamentPlugin.getCombatTag().getTagManager().untag(ps.getUniqueId());
        }*/
    }
}
