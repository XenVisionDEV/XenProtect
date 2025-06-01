package net.xenvision.xenprotect.lang;

import net.xenvision.xenprotect.XenProtect;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LangManager {
    private final XenProtect plugin;
    private YamlConfiguration lang;
    private String langCode;

    public LangManager(XenProtect plugin, String langCode) {
        this.plugin = plugin;
        this.langCode = langCode;
        reload();
    }

    public void reload() {
        File langFile = new File(plugin.getDataFolder() + "/lang", langCode + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + langCode + ".yml", false);
        }
        try {
            lang = YamlConfiguration.loadConfiguration(new InputStreamReader(
                    new File(plugin.getDataFolder() + "/lang", langCode + ".yml").toURI().toURL().openStream(),
                    StandardCharsets.UTF_8
            ));
        } catch (Exception e) {
            plugin.getLogger().warning("§c[LangManager] Не удалось загрузить " + langCode + ".yml: " + e.getMessage());
        }
    }

    public List<String> getMessageList(String key) {
        List<String> message = lang.getStringList(key);
        return message.isEmpty() ? List.of(ChatColor.RED + "Сообщение не найдено: " + key) : colorize(message);
    }

    public String getMessage(String key) {
        String msg = lang.getString(key, ChatColor.RED + "Сообщение не найдено: " + key);
        return colorize(msg);
    }

    private List<String> colorize(List<String> lines) {
        return lines.stream().map(this::colorize).toList();
    }

    private String colorize(String text) {
        // Поддержка HEX цветов &#ABCDEF и стандартных &-цветов
        String colored = text.replaceAll("&#([A-Fa-f0-9]{6})", "§x§$1".replaceAll("([A-Fa-f0-9])", "§$1"));
        return ChatColor.translateAlternateColorCodes('&', colored);
    }
}