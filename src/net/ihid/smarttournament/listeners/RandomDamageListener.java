package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Mikey on 7/20/2016.
 */
public class RandomDamageListener implements Listener {
    private final TournamentAPI tournamentAPI;

    public RandomDamageListener(TournamentPlugin plugin) {
        this.tournamentAPI = TournamentPlugin.getTournamentAPI();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent evt) {
        if(tournamentAPI.isTournamentRunning()) {
            Player entity;

            if(evt.getEntity().getType() != EntityType.PLAYER) {
                return;
            }
            entity = (Player) evt.getEntity();

            if(tournamentAPI.isInTournament(entity)) {
                if(evt.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    evt.setCancelled(true);
                }
            }
        }
    }
}
