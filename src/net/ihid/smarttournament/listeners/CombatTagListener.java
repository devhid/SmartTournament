package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.hooks.HookHandler;
import net.ihid.smarttournament.managers.MainManager;
import net.minelink.ctplus.event.PlayerCombatTagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Mikey on 7/20/2016.
 */
class CombatTagListener implements Listener {
    private final MainManager mainManager;
    private final HookHandler hookHandler;

    CombatTagListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        this.hookHandler = TournamentPlugin.getHookHandler();

        if(hookHandler.getCombatTagPlusHook().isEnabled()) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onDamage(PlayerCombatTagEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if(mainManager.isInTournament(evt.getAttacker()) || mainManager.isInTournament(evt.getVictim())) {
                evt.setCancelled(true);
            }
        }
    }
}
