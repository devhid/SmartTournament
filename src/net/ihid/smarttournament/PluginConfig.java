package net.ihid.smarttournament;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

/**
 * Created by Mikey on 4/25/2016.
 */
public class PluginConfig {

    private final String fileName;
    private final JavaPlugin plugin;

    private File file;
    private YamlConfiguration config;

    public PluginConfig(JavaPlugin plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("The plugin cannot be null.");
        }

        this.plugin = plugin;
        this.fileName = fileName;

        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null)
            throw new IllegalStateException();

        this.file = new File(plugin.getDataFolder(), fileName);
    }

    public void reloadConfig() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), fileName);
        }
        config = YamlConfiguration.loadConfiguration(file);

        Reader stream = null;
        try {
            stream = new InputStreamReader(plugin.getResource(fileName), "UTF8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        if (stream != null) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(stream);
            config.setDefaults(cfg);
        }
    }

    public void saveConfig() {
        if (config == null || file == null) {
            return;
        }

        try {
            getConfig().save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }

    public YamlConfiguration getConfig() {
        if (config == null) {
            this.reloadConfig();
        }

        return config;
    }
}
