package net.xenvision.xenprotect.claim;

import org.bukkit.Location;

import java.util.*;

public class ClaimRegion {
    private final UUID owner;
    private final Set<UUID> coOwners = new HashSet<>();
    private final Set<UUID> members = new HashSet<>();
    private final Location center;
    private final int radius;
    private final String blockType;
    private int durability;
    private final int maxDurability;

    public ClaimRegion(UUID owner, Location center, int radius, String blockType, int durability) {
        this.owner = owner;
        this.center = center;
        this.radius = radius;
        this.blockType = blockType;
        this.durability = durability;
        this.maxDurability = durability;
    }

    public boolean isInside(Location loc) {
        if (!loc.getWorld().equals(center.getWorld())) return false;
        return Math.abs(loc.getBlockX() - center.getBlockX()) <= radius
                && Math.abs(loc.getBlockY() - center.getBlockY()) <= radius
                && Math.abs(loc.getBlockZ() - center.getBlockZ()) <= radius;
    }

    public UUID getOwner() { return owner; }
    public Location getCenter() { return center; }
    public int getRadius() { return radius; }
    public String getBlockType() { return blockType; }
    public int getDurability() { return durability; }
    public int getMaxDurability() { return maxDurability; }
    public void setDurability(int durability) { this.durability = durability; }

    public Set<UUID> getCoOwners() { return coOwners; }
    public Set<UUID> getMembers() { return members; }

    public void addCoOwner(UUID uuid) { coOwners.add(uuid); }
    public void removeCoOwner(UUID uuid) { coOwners.remove(uuid); }
    public void addMember(UUID uuid) { members.add(uuid); }
    public void removeMember(UUID uuid) { members.remove(uuid); }
}