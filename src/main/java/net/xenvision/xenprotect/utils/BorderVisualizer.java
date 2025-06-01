package net.xenvision.xenprotect.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.World;

public class BorderVisualizer {
    public static void showBorders(Player player, Location center, int radius, int durationSeconds) {
        World world = center.getWorld();
        int minX = center.getBlockX() - radius;
        int maxX = center.getBlockX() + radius;
        int minY = Math.max(center.getBlockY() - 1, world.getMinHeight());
        int maxY = Math.min(center.getBlockY() + 1, world.getMaxHeight() - 1);
        int minZ = center.getBlockZ() - radius;
        int maxZ = center.getBlockZ() + radius;

        // По всем граням и углам
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= maxY; y += (maxY - minY)) {
                    Location loc = new Location(world, x + 0.5, y + 0.1, z + 0.5);
                    player.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0,
                            new Particle.DustOptions(org.bukkit.Color.LIME, 1.5f));
                }
            }
        }
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x += (maxX - minX)) {
                for (int z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    Location loc = new Location(world, x + 0.5, y + 0.1, z + 0.5);
                    player.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0,
                            new Particle.DustOptions(org.bukkit.Color.LIME, 1.5f));
                }
            }
        }
        // Можно сделать повтор через BukkitRunnable для длительности
    }
}