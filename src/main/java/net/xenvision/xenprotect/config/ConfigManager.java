package net.xenvision.xenprotect.config;

import net.xenvision.xenprotect.XenProtect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final XenProtect plugin;
    private FileConfiguration config;

    public ConfigManager(XenProtect plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void save() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().warning("§c[ConfigManager] Не удалось сохранить config.yml: " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}