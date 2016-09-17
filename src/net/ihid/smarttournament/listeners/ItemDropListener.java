package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;

class ItemDropListener implements Listener {
    private final MainManager mainManager;

    ItemDropListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
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

            if(evt.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                return;
            }
            
            if(evt.getClickedInventory().getType() != InventoryType.PLAYER) {
                return;
            }
            
            if(mainManager.isInTournament((Player) evt.getWhoClicked())) {
                evt.setCancelled(true);
            }
        }
    }
}
