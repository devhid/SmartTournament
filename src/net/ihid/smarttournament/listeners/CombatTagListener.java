package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.MainManager;
import net.minelink.ctplus.event.PlayerCombatTagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

class CombatTagListener implements Listener {
    private final MainManager mainManager;

    CombatTagListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();

        if(TournamentPlugin.getHookHandler().getCombatTagPlusHook().isEnabled()) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onDamage(PlayerCombatTagEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if(evt.getVictim() != null) {
                if(mainManager.isInTournament(evt.getVictim())) {
                    evt.setCancelled(true);
                }
            }

            else {
                if(evt.getAttacker() != null) {
                    if(mainManager.isInTournament(evt.getAttacker())) {
                        evt.setCancelled(true);
                    }
                }
            }

        }
    }
}
