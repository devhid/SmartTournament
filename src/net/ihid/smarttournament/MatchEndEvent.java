package net.ihid.smarttournament;

import net.ihid.smarttournament.objects.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Mikey on 7/3/2016.
 */
public class MatchEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Match match;

    public MatchEndEvent(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
