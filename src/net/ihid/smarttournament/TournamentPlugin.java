package net.ihid.smarttournament;

import lombok.Getter;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.config.PluginConfig;
import net.minelink.ctplus.CombatTagPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TournamentPlugin extends JavaPlugin {
    @Getter
    private static TournamentAPI tournamentAPI;

    @Getter
    private final PluginConfig rawConfig;

    private static CombatTagPlus combatTag;

    public TournamentPlugin() {
        this.rawConfig = new PluginConfig(this, "settings.yml");
    }

    @Override
    public void onEnable() {
        saveDefault();

        tournamentAPI = new TournamentAPI();

        loadCommands();
        loadListeners();

        try {
            combatTag = (CombatTagPlus) Bukkit.getServer().getPluginManager().getPlugin("CombatTagPlus");
        } catch(NoClassDefFoundError error) {
            System.out.println("[SmartTournament] WARN: Optional dependency CombatTagPlus was not found.");
        }
    }

    @Override
    public void onDisable() {
        tournamentAPI.endTournament();
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

    public static CombatTagPlus getCombatTag() throws NoClassDefFoundError {
        try {
            return combatTag;
        } catch(NoClassDefFoundError error) {

        }
        return null;
    }

    @Override
    public YamlConfiguration getConfig() {
        return getRawConfig().getConfig();
    }

    public static TournamentPlugin getInstance() {
        return TournamentPlugin.getPlugin(TournamentPlugin.class);
    }
}
