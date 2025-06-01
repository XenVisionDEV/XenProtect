package net.xenvision.xenprotect.claim;

import net.xenvision.xenprotect.XenProtect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ClaimStorage {
    private final XenProtect plugin;
    private final File file;
    private YamlConfiguration yaml;

    public ClaimStorage(XenProtect plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "claims.yml");
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void save(List<ClaimRegion> claims) {
        yaml = new YamlConfiguration();
        int idx = 0;
        for (ClaimRegion claim : claims) {
            String path = "claims." + idx++;
            yaml.set(path + ".owner", claim.getOwner().toString());
            yaml.set(path + ".world", claim.getCenter().getWorld().getName());
            yaml.set(path + ".x", claim.getCenter().getBlockX());
            yaml.set(path + ".y", claim.getCenter().getBlockY());
            yaml.set(path + ".z", claim.getCenter().getBlockZ());
            yaml.set(path + ".radius", claim.getRadius());
            yaml.set(path + ".blockType", claim.getBlockType());
            yaml.set(path + ".durability", claim.getDurability());
            yaml.set(path + ".maxDurability", claim.getMaxDurability());
            yaml.set(path + ".coOwners", claim.getCoOwners().stream().map(UUID::toString).toList());
            yaml.set(path + ".members", claim.getMembers().stream().map(UUID::toString).toList());
        }
        try {
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Не удалось сохранить claims.yml: " + e.getMessage());
        }
    }

    public List<ClaimRegion> load() {
        List<ClaimRegion> claims = new ArrayList<>();
        if (!yaml.contains("claims")) return claims;

        for (String key : yaml.getConfigurationSection("claims").getKeys(false)) {
            String path = "claims." + key + ".";
            UUID owner = UUID.fromString(yaml.getString(path + "owner"));
            String world = yaml.getString(path + "world");
            int x = yaml.getInt(path + "x");
            int y = yaml.getInt(path + "y");
            int z = yaml.getInt(path + "z");
            int radius = yaml.getInt(path + "radius");
            String blockType = yaml.getString(path + "blockType");
            int durability = yaml.getInt(path + "durability");
            int maxDurability = yaml.getInt(path + "maxDurability");
            Location center = new Location(Bukkit.getWorld(world), x, y, z);
            ClaimRegion claim = new ClaimRegion(owner, center, radius, blockType, maxDurability);
            claim.setDurability(durability);

            // coOwners & members
            List<String> coOwners = yaml.getStringList(path + "coOwners");
            List<String> members = yaml.getStringList(path + "members");
            coOwners.forEach(uuid -> claim.addCoOwner(UUID.fromString(uuid)));
            members.forEach(uuid -> claim.addMember(UUID.fromString(uuid)));

            claims.add(claim);
        }
        return claims;
    }
}