package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Mikey on 7/20/2016.
 */
public class ItemDropListener implements Listener {
    private final MainManager mainManager;

    public ItemDropListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent evt) {
        if (mainManager.isTournamentRunning()) {
            if (TournamentPlugin.getInstance().getConfig().getBoolean("configuration.when-fighting.prevent-drop-items") && mainManager.isInTournament(evt.getPlayer())) {
                evt.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorDrop(InventoryClickEvent evt) {
        if(mainManager.isTournamentRunning() && TournamentPlugin.getInstance().getConfig().getBoolean("configuration.when-fighting.prevent-drop-items") ) {
            if(evt.getWhoClicked().getType() != EntityType.PLAYER) {
                return;
            }
            
            if(evt.getClickedInventory().getType() != InventoryType.PLAYER) {
                return;
            }
            
            if(mainManager.isInTournament((Player) evt.getWhoClicked())) {
                switch (evt.getAction()) {
                    case DROP_ONE_SLOT: evt.setCancelled(true); break;
                    case DROP_ALL_SLOT: evt.setCancelled(true); break;
                    case DROP_ONE_CURSOR: evt.setCancelled(true); break;
                    case DROP_ALL_CURSOR: evt.setCancelled(true); break;
                    default:
                        break;
                }
            }
        }
    }
}
