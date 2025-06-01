package net.xenvision.xenprotect.listeners;

import net.xenvision.xenprotect.XenProtect;
import net.xenvision.xenprotect.claim.ClaimManager;
import net.xenvision.xenprotect.claim.ClaimRegion;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Locale;
import java.util.UUID;

public class BlockPlaceListener implements Listener {
    private final XenProtect plugin;
    private final ClaimManager claimManager;

    public BlockPlaceListener(XenProtect plugin) {
        this.plugin = plugin;
        this.claimManager = plugin.getClaimManager();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        // Проверяем, разрешен ли этот блок для привата
        String blockKey = block.getType().name().toLowerCase(Locale.ROOT);
        ConfigurationSection section = plugin.getConfigManager().getConfig().getConfigurationSection("claims." + blockKey);
        if (section == null) return; // Не приватный блок

        if (!claimManager.canCreateClaim(player)) {
            player.sendMessage(plugin.getLangManager().getMessage("no-permission"));
            event.setCancelled(true);
            return;
        }

        int radius = section.getInt("radius");
        int durability = section.getInt("durability");
        UUID owner = player.getUniqueId();

        // Проверка на пересечение с другими приватами
        if (claimManager.getClaimAt(block.getLocation()) != null) {
            player.sendMessage("§cЗдесь уже есть приват!");
            event.setCancelled(true);
            return;
        }

        // Создаем приват
        ClaimRegion claim = new ClaimRegion(owner, block.getLocation(), radius, blockKey, durability);
        claimManager.addClaim(claim);

        player.sendMessage(plugin.getLangManager().getMessage("claim-created")
                .replace("%owner%", player.getName()));
        // TODO: Создание голограммы, визуализация границ и т.д.
    }
}