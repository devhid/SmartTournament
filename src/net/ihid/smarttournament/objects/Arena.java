package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class Arena {
    @Getter
    private final String arenaName;

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
