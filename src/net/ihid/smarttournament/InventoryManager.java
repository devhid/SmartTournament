package net.ihid.smarttournament;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

/**
 * Created by Mikey on 6/12/2016.
 */
public class InventoryManager {

    /* Armor */
    private static final ItemStack HELMET = new ItemStack(Material.DIAMOND_HELMET, 1);
    private static final ItemStack CHESTPLATE = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
    private static final ItemStack LEGGINGS = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
    private static final ItemStack BOOTS = new ItemStack(Material.DIAMOND_BOOTS, 1);

    /* Weapon */
    private static final ItemStack SWORD = new ItemStack(Material.DIAMOND_SWORD, 1);

    /* Potions */
    private static final ItemStack HEALTH_POTION = new ItemStack(Material.POTION, 1, (short) 16421);

    /* Effects */
    private static final Collection<PotionEffect> EFFECTS = new ImmutableSet.Builder<PotionEffect>()
            .add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1))
            .add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0))
            .build();

    public static void equip(Player player) {
        final PlayerInventory inv = player.getInventory();

        HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 3);

        inv.setHelmet(HELMET);
        inv.setChestplate(CHESTPLATE);
        inv.setLeggings(LEGGINGS);
        inv.setBoots(BOOTS);

        inv.setItem(0, SWORD);

        for(int i=1; i < 9; i++) {
            inv.setItem(i, HEALTH_POTION);
        }

        EFFECTS.forEach(player::addPotionEffect);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setExhaustion(0);
        player.setSaturation(20);
        player.setGameMode(GameMode.SURVIVAL);
    }
}
