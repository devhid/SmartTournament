package net.ihid.smarttournament;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Created by Mikey on 6/7/2016.
 */
public class SavedPlayerState {

    private final Player player;
    private boolean reverted = false;

    /* Hunger */
    private final int hunger;
    private final float saturation;
    private final float exhaustion;

    /* Exp */
    private final float exp;

    /* Health */
    private final double health;

    /* Location */
    private final Location location;
    private final Vector velocity;

    /* Effects */
    private final Collection<PotionEffect> effects;

    /* Misc */
    private final GameMode gameMode;

    public SavedPlayerState(Player player) {
        this.player = player;

        this.hunger = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.exhaustion = player.getExhaustion();

        this.exp = player.getExp();

        this.health = player.getHealth();

        this.location = player.getLocation();
        this.velocity = player.getVelocity();

        this.effects = player.getActivePotionEffects();

        this.gameMode = player.getGameMode();
    }

    public void ensureDefaults(GameMode gameMode) {
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExhaustion(0);

        player.setExp(0);

        player.setHealth(20);

        for (PotionEffect e : effects) {
            player.removePotionEffect(e.getType());
        }

        player.setGameMode(gameMode);
    }

    public void revert() {
        if (reverted) {
            return;
        }

        player.setFoodLevel(hunger);
        player.setSaturation(saturation);
        player.setExhaustion(exhaustion);

        player.setExp(exp);

        player.setHealth(health);

        for (PotionEffect e : player.getActivePotionEffects()) {
            player.removePotionEffect(e.getType());
        }

        player.addPotionEffects(effects);

        player.setGameMode(gameMode);

        reverted = true; // exception in teleportation handler isnt our fault
    }

    public boolean isReverted() {
        return reverted;
    }
}
