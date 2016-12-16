package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

class PlayerJoinListener implements Listener {
    private TournamentPlugin plugin;

    PlayerJoinListener(TournamentPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        List<String> list = plugin.getConfig().getStringList("player-logout-data");

        if(list.contains(evt.getPlayer().getUniqueId().toString())) {
            evt.getPlayer().teleport(TournamentPlugin.getMainManager().getWorldSpawn());

            list.remove(evt.getPlayer().getUniqueId().toString());
            plugin.getConfig().set("player-logout-data", list);
            plugin.getRawConfig().saveConfig();
        }
    }
}
