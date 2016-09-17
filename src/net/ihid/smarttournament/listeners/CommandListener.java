package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

class CommandListener implements Listener {
    private final MainManager mainManager;
    private final YamlConfiguration config;

    CommandListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        this.config = plugin.getConfig();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent evt) {
        if(config.getBoolean("configuration.disable-commands-in-tournament")) {
            if (!mainManager.isTournamentRunning()) {
                return;
            }

            if (!mainManager.isInTournament(evt.getPlayer())) {
                return;
            }

            if(evt.getPlayer().hasPermission("smarttournament.chatbypass")) {
                return;
            }

            if (evt.getMessage().charAt(0) == '/') {
                if (!config.getStringList("configuration.cmd-whitelist").stream().anyMatch(s -> s.trim().equalsIgnoreCase(evt.getMessage().trim().split(" ")[0]))) {
                    evt.getPlayer().sendMessage(Lang.COMMAND_USE_DENIED.toString());
                    evt.setCancelled(true);
                }
            }
        }
    }
}
