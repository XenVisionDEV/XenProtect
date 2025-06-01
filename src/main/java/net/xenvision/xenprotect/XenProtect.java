package net.xenvision.xenprotect;

import net.xenvision.xenprotect.listener.BlockPlaceListener;
import net.xenvision.xenprotect.claim.ClaimManager;
import net.xenvision.xenprotect.config.ConfigManager;
import net.xenvision.xenprotect.lang.LangManager;
import org.bukkit.plugin.java.JavaPlugin;

public class XenProtect extends JavaPlugin {

    private static XenProtect instance;
    private ConfigManager configManager;
    private LangManager langManager;
    private ClaimManager claimManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        claimManager = new ClaimManager(this);
        langManager = new LangManager(this, "ru"); // Можно добавить выбор языка через config.yml

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);

        getLogger().info("XenProtect enabled!");
    }

    @Override
    public void onDisable() {
        claimManager.saveAll();
        getLogger().info("XenProtect disabled!");
    }

    public static XenProtect getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public ClaimManager getClaimManager() {
        return claimManager;
    }
}
