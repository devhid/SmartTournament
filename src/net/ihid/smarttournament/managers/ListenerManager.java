package net.ihid.smarttournament.managers;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.listeners.*;
import org.bukkit.event.Listener;

/**
 * Created by Mikey on 7/21/2016.
 */
public class ListenerManager {
    @Getter
    private CombatTagListener combatTagListener;

    @Getter
    private CommandListener commandListener;

    @Getter
    private ItemDropListener itemDropListener;

    @Getter
    private PlayerDamageListener playerDamageListener;

    @Getter
    private PlayerQuitListener playerQuitListener;

    @Getter
    private RandomDamageListener randomDamageListener;

    public ListenerManager(TournamentPlugin plugin) {
        this.combatTagListener = new CombatTagListener(plugin);
        this.commandListener = new CommandListener(plugin);
        this.itemDropListener = new ItemDropListener(plugin);
        this.playerDamageListener = new PlayerDamageListener(plugin);
        this.playerQuitListener = new PlayerQuitListener(plugin);
        this.randomDamageListener = new RandomDamageListener(plugin);
    }
}
