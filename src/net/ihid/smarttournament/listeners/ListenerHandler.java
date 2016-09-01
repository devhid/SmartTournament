package net.ihid.smarttournament.listeners;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;

/**
 * Created by Mikey on 7/23/2016.
 */
public class ListenerHandler {
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
    private BlockBreakListener blockBreakListener;

    @Getter
    private CrackShotDamageListener crackShotDamageListener;

    @Getter
    private PlayerJoinListener playerJoinListener;

    public ListenerHandler(TournamentPlugin plugin) {
        this.combatTagListener = new CombatTagListener(plugin);
        this.commandListener = new CommandListener(plugin);
        this.itemDropListener = new ItemDropListener(plugin);
        this.playerDamageListener = new PlayerDamageListener(plugin);
        this.playerQuitListener = new PlayerQuitListener(plugin);
        this.blockBreakListener = new BlockBreakListener(plugin);
        this.crackShotDamageListener = new CrackShotDamageListener(plugin);
        this.playerJoinListener = new PlayerJoinListener(plugin);
    }
}
