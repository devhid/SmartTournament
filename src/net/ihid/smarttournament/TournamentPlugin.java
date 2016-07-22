package net.ihid.smarttournament;

import lombok.Getter;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.config.PluginConfig;
import net.ihid.smarttournament.hooks.HookManager;
import net.ihid.smarttournament.listeners.CombatTagListener;
import net.ihid.smarttournament.listeners.CommandListener;
import net.ihid.smarttournament.listeners.ItemDropListener;
import net.ihid.smarttournament.listeners.RandomDamageListener;
import net.ihid.smarttournament.managers.ListenerManager;
import net.minelink.ctplus.CombatTagPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class TournamentPlugin extends JavaPlugin {
    @Getter
    private static TournamentAPI tournamentAPI;

    @Getter
    private static HookManager hookManager;

    @Getter
    private ListenerManager listenerManager;

    @Getter
    private final PluginConfig rawConfig;

    public TournamentPlugin() {
        this.rawConfig = new PluginConfig(this, "settings.yml");
    }

    @Override
    public void onEnable() {
        saveDefault();

        hookManager = new HookManager();
        tournamentAPI = new TournamentAPI();

        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        tournamentAPI.endTournament();
    }

    private void loadCommands() {
        getCommand("tournament").setExecutor(new CommandTournament(this));
    }

    private void loadListeners() {
        listenerManager = new ListenerManager(this);
    }

    private void saveDefault() {
        rawConfig.saveDefaultConfig();
    }

    @Override
    public YamlConfiguration getConfig() {
        return getRawConfig().getConfig();
    }

    public static TournamentPlugin getInstance() {
        return TournamentPlugin.getPlugin(TournamentPlugin.class);
    }
}
