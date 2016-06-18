package net.ihid.smarttournament.player;

import net.ihid.smarttournament.TournamentPlugin;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mikey on 6/14/2016.
 */
public class NewPlayerState {
    private TournamentPlugin plugin = TournamentPlugin.getInstance();

    public void modifyPlayer(Player player) {
        setDefaultState(player);
        addEffects(player);
        addArmor(player);
        addInventory(player);
    }

    public void setDefaultState(Player player) {
        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-max-health")) {
            player.setHealth(20.0);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-max-hunger")) {
            player.setFoodLevel(20);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-min-exhaustion")) {
            player.setExhaustion(0);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-max-saturation")) {
            player.setSaturation(20);
        }

        if(plugin.getConfig().getBoolean("configuration.when-fighting.ensure-survival-gamemode")) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    private void addInventory(Player player) {
        for(String slot: plugin.getConfig().getConfigurationSection("configuration.when-fighting.inventory").getKeys(false)) {
            String path = "configuration.when-fighting.inventory." + slot + ".";
            ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString(path + ".material")), plugin.getConfig().getInt(path + ".amount"),
                    (short) plugin.getConfig().getInt(path + ".material-id"));

            for(String enc: plugin.getConfig().getConfigurationSection(path + "enchantments").getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(enc.toUpperCase());
                if(enchantment == null) {
                    System.err.println("Invalid enchantment type. Please check if you spelt the enchantments correctly.");
                    break;
                }
                int level = plugin.getConfig().getInt(path + "enchantments." + enc);

                item.addUnsafeEnchantment(enchantment, level);
            }
            List<Integer> slots = plugin.getConfig().getIntegerList(path + "slot-numbers");

            for(int i = 0; i < slots.size(); i++) {
                player.getInventory().setItem(slots.get(i), item);
            }
        }
    }

    private void addArmor(Player player) {
        ItemStack[] armorContents = new ItemStack[4];
        int count = 3;

        for(String slot: plugin.getConfig().getConfigurationSection("configuration.when-fighting.armor").getKeys(false)) {
            String path = "configuration.when-fighting.armor." + slot + ".";
            ItemStack armor = new ItemStack(Material.getMaterial(plugin.getConfig().getString(path + "material")), 1);

            Set<String> enchants = plugin.getConfig().getConfigurationSection(path + "enchantments").getKeys(false);

            if(enchants.size() != 0) {
                for(String enc : enchants) {
                    Enchantment enchantment = Enchantment.getByName(enc.toUpperCase());
                    if (enchantment == null) {
                        System.err.println("Invalid enchantment type. Please check if you spelt the enchantments correctly.");
                        break;
                    }
                    int level = plugin.getConfig().getInt(path + "enchantments." + enc);
                    armor.addUnsafeEnchantment(enchantment, level);
                }
            }
            armorContents[count] = armor;
            count--;
        }

        player.getInventory().setArmorContents(armorContents);
    }

    private void addEffects(Player player) {
        Set<String> effects = plugin.getConfig().getConfigurationSection("configuration.when-fighting.effects").getKeys(false);

        for(String effect: effects) {
            String path = "configuration.when-fighting.effects." + effect;

            PotionEffectType type = PotionEffectType.getByName(effect.toUpperCase());
            int level = plugin.getConfig().getInt(path)!=1 ? plugin.getConfig().getInt(path)-2: 0;
            int duration = plugin.getConfig().getInt("configuration.match-duration");

            player.addPotionEffect(new PotionEffect(type, duration, level));
        }
    }
}
