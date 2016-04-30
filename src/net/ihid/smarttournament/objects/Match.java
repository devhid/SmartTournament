package net.ihid.smarttournament.objects;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import java.util.Set;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Match {
    @Getter
    private Player firstPlayer, secondPlayer;

    @Getter @Setter
    private Arena arena;

    @Getter @Setter
    private Player winner;

    @Getter
    private long duration;

    @Getter @Setter
    private int matchId;

    public Match(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.duration = 20L /*ticks*/ * 60 /*seconds*/ * 3 /*minutes*/;
    }

    public Set<Player> toSet() {
        return Sets.newHashSet(firstPlayer, secondPlayer);
    }
}
