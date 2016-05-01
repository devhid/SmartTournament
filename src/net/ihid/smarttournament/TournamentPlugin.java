package net.ihid.smarttournament;

import lombok.Getter;
import net.ihid.smarttournament.commands.CommandTournament;
import net.ihid.smarttournament.data.PluginConfig;
import net.ihid.smarttournament.listeners.TournamentListener;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Mikey on 4/24/2016.
 */
public class TournamentPlugin extends JavaPlugin {
    public static TournamentPlugin i;

    @Getter
    private static MainManager mainManager;

    @Getter
    private final PluginConfig rawConfig = new PluginConfig(this, "settings.yml");

    @Override
    public void onEnable() {
        i = this;
        saveDefault();

        mainManager = new MainManager();
        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {

    }

    private void loadCommands() {
        getCommand("tournament").setExecutor(new CommandTournament(this));
    }

    private void loadListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new TournamentListener(this), this);
    }

    private void saveDefault() {
        rawConfig.saveDefaultConfig();
    }

    @Override
    public YamlConfiguration getConfig() {
        return getRawConfig().getConfig();
    }

    public void disable() {
        setEnabled(false);
    }
}
