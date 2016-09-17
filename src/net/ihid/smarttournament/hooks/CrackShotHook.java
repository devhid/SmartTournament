package net.ihid.smarttournament.hooks;

import com.shampaggon.crackshot.CSDirector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class CrackShotHook implements Hook {
    private final PluginManager pluginManager;
    private final String pluginName;

    CrackShotHook() {
        this.pluginManager = Bukkit.getServer().getPluginManager();
        this.pluginName = "CrackShot";
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

    public CSDirector get() {
        return (CSDirector) getPlugin();
    }
}
