package net.xenvision.xenprotect.listeners;

import net.xenvision.xenprotect.XenProtect;
import net.xenvision.xenprotect.claim.ClaimManager;
import net.xenvision.xenprotect.claim.ClaimRegion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final XenProtect plugin;
    private final ClaimManager claimManager;

    public BlockBreakListener(XenProtect plugin) {
        this.plugin = plugin;
        this.claimManager = plugin.getClaimManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        ClaimRegion claim = claimManager.getClaimAt(block.getLocation());
        if (claim == null) return;

        // Проверяем, ломает ли блок-центр привата
        if (!block.getLocation().equals(claim.getCenter())) return;

        // Только владелец может ломать свой приват
        if (!player.getUniqueId().equals(claim.getOwner())) {
            player.sendMessage(plugin.getLangManager().getMessage("no-permission"));
            event.setCancelled(true);
            return;
        }

        // Удаляем приват
        claimManager.removeClaim(claim);
        player.sendMessage(plugin.getLangManager().getMessage("claim-removed")
                .replace("%owner%", player.getName()));
        // TODO: удалять голограмму, визуализацию и т.д.
    }
}