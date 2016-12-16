package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.List;

class PlayerQuitListener implements Listener {
    private final TournamentPlugin plugin;
    private final MainManager mainManager;

    PlayerQuitListener(TournamentPlugin plugin) {
        this.plugin = plugin;
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        if(mainManager.isTournamentRunning()) {
            if(!mainManager.isInTournament(evt.getPlayer())) {
                return;
            }

            final Player player = evt.getPlayer();
            mainManager.removeFromTournament(player);
            TournamentPlugin.getHookHandler().getVanishNoPacketHook().unvanish(player);

            List<String> list = plugin.getConfig().getStringList("player-logout-data");

            if(!list.contains(player.getUniqueId().toString())) {
                list.add(player.getUniqueId().toString());
                plugin.getConfig().set("player-logout-data", list);
                plugin.getRawConfig().saveConfig();
            }

            if(mainManager.getTournament().getStage() == TournamentStage.ACTIVE) {
                mainManager.getMatches().stream()
                        .filter(match -> match.toSet().contains(player))
                        .findAny()
                        .ifPresent(match -> {
                            match.setWinner(player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator());

                            mainManager.addMatchWinner(match.getWinner());
                            mainManager.endMatch(match);
                        });
            }
        }
    }
}
