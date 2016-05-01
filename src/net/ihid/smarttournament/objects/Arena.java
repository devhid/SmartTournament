package net.ihid.smarttournament.objects;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.ArenaManager;
import org.bukkit.Location;

import java.util.Set;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Arena {
    private ArenaManager arenaManager = TournamentPlugin.getMainManager().getArenaManager();

    @Getter
    private String arenaName;

    @Getter @Setter
    private Location firstLoc, secondLoc;

    @Setter @Getter
    private boolean occupied;

    public Arena(String arenaName, Location firstLoc, Location secondLoc) {
        this.arenaName = arenaName;
        this.firstLoc = firstLoc;
        this.secondLoc = secondLoc;
        this.occupied = false;
    }

    public Set<Location> toSet() {
        return Sets.newHashSet(firstLoc, secondLoc);
    }
}
