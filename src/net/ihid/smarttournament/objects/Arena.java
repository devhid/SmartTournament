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
    @Getter
    private String arenaName;

    @Setter @Getter
    private boolean occupied;

    @Getter @Setter
    private Location firstLocation, secondLocation;

    public Arena(String arenaName, Location firstLocation, Location secondLocation) {
        this.occupied = false;
        this.arenaName = arenaName;
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
    }
}
