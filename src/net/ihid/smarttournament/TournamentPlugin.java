package net.ihid.smarttournament;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.ihid.smarttournament.commands.CommandTournament;
import net.ihid.smarttournament.data.PluginConfig;
import net.ihid.smarttournament.listeners.TournamentListener;
import net.ihid.smarttournament.managers.ArenaManager;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.managers.MatchManager;
import net.ihid.smarttournament.managers.TournamentManager;
import net.ihid.smarttournament.objects.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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

        mainManager = new MainManager();

        saveDefault();
        loadArenas();
    }

    @Override
    public void onDisable() {

    }

    public void loadArenas() {
        for(String str: this.getConfig().getConfigurationSection("arenas").getKeys(false)) {
            String path = "arenas." + str + ".";
            
            Location firstLoc = new Location(Bukkit.getWorld(this.getConfig().getString(path + "1.world")),
                    this.getConfig().getInt(path + "1.x"),
                    this.getConfig().getInt(path + "1.y"),
                    this.getConfig().getInt(path + "1.z"),
                    this.getConfig().getInt(path + "1.pitch"),
                    this.getConfig().getInt(path + "1.yaw"));

            Location secondLoc = new Location(Bukkit.getWorld(this.getConfig().getString(path + "2.world")),
                    this.getConfig().getInt(path + "2.x"),
                    this.getConfig().getInt(path + "2.y"),
                    this.getConfig().getInt(path + "2.z"),
                    this.getConfig().getInt(path + "2.pitch"),
                    this.getConfig().getInt(path + "2.yaw"));
            mainManager.getArenaManager().getArenas().add(new Arena(str, firstLoc, secondLoc));

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

    @Override
    public YamlConfiguration getConfig() {
        return getRawConfig().getConfig();
    }

    public void disable() {
        setEnabled(false);
    }
}
