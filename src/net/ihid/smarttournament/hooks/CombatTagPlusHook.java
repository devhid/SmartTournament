package net.ihid.smarttournament.hooks;

import lombok.Getter;
import net.minelink.ctplus.CombatTagPlus;
import net.minelink.ctplus.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Created by Mikey on 7/20/2016.
 */
public class CombatTagPlusHook implements Hook {
    private final PluginManager pluginManager;
    private final String pluginName;

    public CombatTagPlusHook() {
        this.pluginManager = Bukkit.getServer().getPluginManager();
        this.pluginName = "CombatTagPlus";
    }

    @Override
    public boolean isEnabled() {
        return pluginManager.getPlugin(pluginName) != null;
    }

    @Override
    public Plugin getPlugin() {
        if(isEnabled()) {
            return pluginManager.getPlugin(pluginName);
        }
        return null;
    }

    @Override
    public String getName() {
        return pluginName;
    }

    public CombatTagPlus get() {
        return (CombatTagPlus) getPlugin();
    }

    public TagManager getTagManager() {
        return get().getTagManager();
    }

}
