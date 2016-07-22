package net.ihid.smarttournament.objects;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.tasks.MatchTask;
import org.bukkit.entity.Player;
import java.util.Set;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Match {
    @Getter
    private Player initiator, opponent;

    @Getter @Setter
    private Arena arena;

    @Getter @Setter
    private Player winner;

    @Getter
    private long duration;

    @Getter @Setter
    private boolean isRunning;

    @Getter @Setter
    private MatchTask matchTask;

    public Match(Player initiator, Player opponent) {
        this.initiator = initiator;
        this.opponent = opponent;
        this.duration = 20L /*ticks*/ * TournamentPlugin.getInstance().getConfig().getInt("configuration.match-duration") /*seconds*/;
        this.isRunning = true;
    }

    public void reset() {
        arena.setOccupied(false);
        setArena(null);
        setRunning(false);
        setWinner(null);
        matchTask.cancel();
    }

    public Set<Player> toSet() {
        return Sets.newHashSet(initiator, opponent);
    }
}
