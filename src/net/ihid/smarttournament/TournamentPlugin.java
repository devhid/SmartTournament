package net.ihid.smarttournament;

import lombok.Getter;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.config.PluginConfig;
import net.ihid.smarttournament.hooks.HookHandler;
import net.ihid.smarttournament.listeners.ListenerHandler;
import net.ihid.smarttournament.managers.MainManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TournamentPlugin extends JavaPlugin {
    @Getter
    private static TournamentAPI tournamentAPI;

    @Getter
    private static MainManager mainManager;

    @Getter
    private static HookHandler hookHandler;

    @Getter
    private ListenerHandler listenerHandler;

    @Getter
    private final PluginConfig rawConfig;

    public TournamentPlugin() {
        this.rawConfig = new PluginConfig(this, "settings.yml");
    }

    @Override
    public void onEnable() {
        saveDefault();

        hookHandler = new HookHandler();

        mainManager = new MainManager(this);
        tournamentAPI = new TournamentAPI();

        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        if(mainManager != null) {
            mainManager.endTournament();
        }
    }

    private void loadCommands() {
        getCommand("tournament").setExecutor(new CommandTournament(this));
    }

    private void loadListeners() {
        listenerHandler = new ListenerHandler(this);
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
