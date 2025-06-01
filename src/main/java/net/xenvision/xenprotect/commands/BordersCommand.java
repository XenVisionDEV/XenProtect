package net.xenvision.xenprotect.commands;

import net.xenvision.xenprotect.XenProtect;
import net.xenvision.xenprotect.claim.ClaimManager;
import net.xenvision.xenprotect.claim.ClaimRegion;
import net.xenvision.xenprotect.util.BorderVisualizer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BordersCommand implements CommandExecutor {
    private final XenProtect plugin;
    private final ClaimManager claimManager;

    public BordersCommand(XenProtect plugin) {
        this.plugin = plugin;
        this.claimManager = plugin.getClaimManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        ClaimRegion claim = claimManager.getClaimAt(player.getLocation());
        if (claim == null) {
            player.sendMessage("§cТы не находишься в привате.");
            return true;
        }
        int duration = plugin.getConfigManager().getConfig().getInt("borders.show_duration", 30);
        BorderVisualizer.showBorders(player, claim.getCenter(), claim.getRadius(), duration);
        player.sendMessage("§aГраницы твоего привата подсвечены на " + duration + " сек!");
        return true;
    }
}