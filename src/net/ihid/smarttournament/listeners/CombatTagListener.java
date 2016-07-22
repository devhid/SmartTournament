package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.hooks.HookManager;
import net.minelink.ctplus.event.PlayerCombatTagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Mikey on 7/20/2016.
 */
public class CombatTagListener implements Listener {
    private final TournamentAPI tournamentAPI;
    private final HookManager hookManager;

    public CombatTagListener(TournamentPlugin plugin) {
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
        this.hookManager = TournamentPlugin.getHookManager();

        if(hookManager.getCombatTagPlusHook().isEnabled()) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onDamage(PlayerCombatTagEvent evt) {
        if(tournamentAPI.isTournamentRunning()) {
            if(tournamentAPI.isInTournament(evt.getAttacker()) || tournamentAPI.isInTournament(evt.getVictim())) {
                evt.setCancelled(true);
            }
        }
    }
}
