package net.ihid.smarttournament.listeners;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.managers.MainManager;
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
    private final MainManager mainManager;

    public RandomDamageListener(TournamentPlugin plugin) {
        this.mainManager = TournamentPlugin.getMainManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent evt) {
        if(mainManager.isTournamentRunning()) {
            Player entity;

            if(evt.getEntity().getType() != EntityType.PLAYER) {
                return;
            }
            entity = (Player) evt.getEntity();

            if(mainManager.isInTournament(entity)) {
                if(evt.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    evt.setCancelled(true);
                }
            }
        }
    }
}
