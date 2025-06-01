package net.xenvision.xenprotect.utils;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.xenvision.xenprotect.XenProtect;
import net.xenvision.xenprotect.claim.ClaimRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HologramHelper {
    private static final Map<String, Hologram> claimHolograms = new HashMap<>();

    private static String getHoloId(ClaimRegion claim) {
        Location center = claim.getCenter();
        // Уникальный id по координатам блока
        return "xenprotect_" + center.getWorld().getName() + "_" + center.getBlockX() + "_" + center.getBlockY() + "_" + center.getBlockZ();
    }

    public static void createOrUpdateHologram(XenProtect plugin, ClaimRegion claim) {
        Location loc = claim.getCenter().clone().add(0.5, 2.2, 0.5);
        String holoId = getHoloId(claim);

        // Если уже есть — обновить строки и переместить
        Hologram hologram = DHAPI.getHologram(holoId);
        String ownerName = Bukkit.getOfflinePlayer(claim.getOwner()).getName();
        if (ownerName == null) ownerName = "???";

        // Линии для голограммы
        var lines = Arrays.asList(
                "§aПриват",
                "§7Владелец: §f" + ownerName
        );

        if (hologram != null) {
            DHAPI.setHologramLines(hologram, lines);
            hologram.teleport(loc);
        } else {
            hologram = DHAPI.createHologram(holoId, loc, lines);
        }
        claimHolograms.put(holoId, hologram);
    }

    public static void removeHologram(ClaimRegion claim) {
        String holoId = getHoloId(claim);
        Hologram hologram = claimHolograms.remove(holoId);
        if (hologram == null) hologram = DHAPI.getHologram(holoId);
        if (hologram != null) hologram.delete();
    }
}