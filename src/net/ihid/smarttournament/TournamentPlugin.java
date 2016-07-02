package net.ihid.smarttournament;

import lombok.Getter;
import net.ihid.smarttournament.config.PluginConfig;
import net.ihid.smarttournament.managers.MainManager;
import net.minelink.ctplus.CombatTagPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Mikey on 4/24/2016.
 */
public class TournamentPlugin extends JavaPlugin {
    @Getter
    private static TournamentAPI tournamentAPI;

    @Getter
    private final PluginConfig rawConfig = new PluginConfig(this, "settings.yml");

    @Override
    public void onEnable() {
        saveDefault();

        tournamentAPI = new TournamentAPI();

        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        if(tournamentAPI.isTournamentRunning()) {
            tournamentAPI.getTournament().end();
        }
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

    public static CombatTagPlus getCombatTag() {
        return (CombatTagPlus) Bukkit.getServer().getPluginManager().getPlugin("CombatTagPlus");
    }

    @Override
    public YamlConfiguration getConfig() {
        return getRawConfig().getConfig();
    }

    public static TournamentPlugin getInstance() {
        return TournamentPlugin.getPlugin(TournamentPlugin.class);
    }
}
