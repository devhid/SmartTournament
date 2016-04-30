package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.objects.Arena;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikey on 4/25/2016.
 */
public class ArenaManager {
    @Getter
    private List<Arena> arenas = new ArrayList<>();

    public void addArena(Arena arena) {
        arenas.add(arena);
    }

    public boolean isOccupied(Arena arena) {
        return arena.isOccupied();
    }

    public Arena getAvailableArena() {
        for(Arena arena: arenas) {
            if(!isOccupied(arena)) {
                return arena;
            }
        }
        return null;
    }
}
