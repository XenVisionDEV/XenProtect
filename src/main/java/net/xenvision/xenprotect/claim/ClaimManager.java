package net.xenvision.xenprotect.claim;

import net.xenvision.xenprotect.XenProtect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimManager {
    private final XenProtect plugin;
    private final Map<UUID, List<ClaimRegion>> claimsByOwner = new HashMap<>();
    private final List<ClaimRegion> allClaims = new ArrayList<>();


    public ClaimManager(XenProtect plugin) {
        this.plugin = plugin;
    }

    public void addClaim(ClaimRegion claim) {
        claimsByOwner.computeIfAbsent(claim.getOwner(), k -> new ArrayList<>()).add(claim);
        allClaims.add(claim);
    }

    public void removeClaim(ClaimRegion claim) {
        claimsByOwner.getOrDefault(claim.getOwner(), Collections.emptyList()).remove(claim);
        allClaims.remove(claim);
    }

    public List<ClaimRegion> getClaimsByOwner(UUID owner) {
        return claimsByOwner.getOrDefault(owner, Collections.emptyList());
    }

    public ClaimRegion getClaimAt(Location loc) {
        for (ClaimRegion claim : allClaims) {
            if (claim.isInside(loc)) return claim;
        }
        return null;
    }

    public int getClaimLimit(Player player) {
        ConfigurationSection limitsSection = plugin.getConfigManager().getConfig().getConfigurationSection("limits");
        int max = 1; // fallback

        for (String key : limitsSection.getKeys(false)) {
            ConfigurationSection section = limitsSection.getConfigurationSection(key);
            String perm = section.getString("permission", "");
            int count = section.getInt("count", 1);

            // Если permission пустая — это дефолт, всегда доступен
            if (perm.isEmpty() || player.hasPermission(perm)) {
                if (count > max) max = count;
            }
        }
        return max;
    }

    public boolean canCreateClaim(Player player) {
        int limit = getClaimLimit(player);
        return getClaimsByOwner(player.getUniqueId()).size() < limit;
    }

    public List<ClaimRegion> getAllClaims() {
        return Collections.unmodifiableList(allClaims);
    }
}