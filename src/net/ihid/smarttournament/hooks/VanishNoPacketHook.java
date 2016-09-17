package net.ihid.smarttournament.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

public class VanishNoPacketHook implements Hook {
    private final PluginManager pluginManager;
    private final String pluginName;

    VanishNoPacketHook() {
        this.pluginManager = Bukkit.getServer().getPluginManager();
        this.pluginName = "VanishNoPacket";
    }

    @Override
    public boolean isEnabled() {
        return pluginManager.getPlugin(pluginName) != null;
    }

    @Override
    public Plugin getPlugin() {
        return (isEnabled()) ? pluginManager.getPlugin(pluginName) : null;
    }

    @Override
    public String getName() {
        return pluginName;
    }

    public VanishPlugin get() {
        return (VanishPlugin) getPlugin();
    }

    public VanishManager getVanishManager() {
        return get().getManager();
    }

    public boolean isVanished(Player player) {
        return getVanishManager().isVanished(player);
    }

    public void unvanish(Player player) {
        if(isVanished(player)) {
            getVanishManager().reveal(player, true, false);
        }
    }

    public void vanish(Player player) {
        getVanishManager().vanish(player, true, false);
    }

}
