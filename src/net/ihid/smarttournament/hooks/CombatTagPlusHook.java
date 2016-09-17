package net.ihid.smarttournament.hooks;

import net.minelink.ctplus.CombatTagPlus;
import net.minelink.ctplus.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class CombatTagPlusHook implements Hook {
    private final PluginManager pluginManager;
    private final String pluginName;

    CombatTagPlusHook() {
        this.pluginManager = Bukkit.getServer().getPluginManager();
        this.pluginName = "CombatTagPlus";
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

    public CombatTagPlus get() {
        return (CombatTagPlus) getPlugin();
    }

    public TagManager getTagManager() {
        return get().getTagManager();
    }

}
