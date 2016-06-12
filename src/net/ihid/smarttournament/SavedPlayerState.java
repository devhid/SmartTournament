package net.ihid.smarttournament;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.Collection;

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

    /* Inventory */
    private final ItemStack[] contents, armorContents;

    /* Exp */
    private final float exp;

    /* Health */
    private final double health;

    /* Velocity */
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

        this.contents = player.getInventory().getContents();
        this.armorContents = player.getInventory().getArmorContents();

        this.exp = player.getExp();

        this.health = player.getHealth();

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

        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armorContents);

        player.setHealth(health);

        for (PotionEffect e : player.getActivePotionEffects()) {
            player.removePotionEffect(e.getType());
        }

        player.addPotionEffects(effects);

        player.setGameMode(gameMode);

        player.setVelocity(velocity);

        reverted = true; // exception in teleportation handler isnt our fault
    }

    public boolean isReverted() {
        return reverted;
    }
}
