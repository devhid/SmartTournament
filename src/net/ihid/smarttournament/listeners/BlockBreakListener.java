package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

class BlockBreakListener implements Listener {
    private final MainManager mainManager;
    private final YamlConfiguration config;

    BlockBreakListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        this.config = plugin.getConfig();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if(mainManager.isInTournament(evt.getPlayer()) && !evt.getPlayer().hasPermission("smarttournament.blockbreak.bypass")) {
                evt.setCancelled(true);
            }
        }
    }
}
