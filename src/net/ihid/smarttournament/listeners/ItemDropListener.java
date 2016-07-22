package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Mikey on 7/20/2016.
 */
public class ItemDropListener implements Listener {
    private final TournamentAPI tournamentAPI;

    public ItemDropListener(TournamentPlugin plugin) {
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent evt) {
        if (tournamentAPI.isTournamentRunning()) {
            if (TournamentPlugin.getInstance().getConfig().getBoolean("configuration.when-fighting.prevent-drop-items") && tournamentAPI.isInTournament(evt.getPlayer())) {
                evt.setCancelled(true);
            }
        }
    }
}
