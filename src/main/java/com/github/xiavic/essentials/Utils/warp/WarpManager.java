package com.github.xiavic.essentials.Utils.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum WarpManager {

    INSTANCE;

    private Collection<Warp> warps = new HashSet<>();

    public boolean isWarp(@NotNull final Location location, boolean useBlockloc) {
        for (final Warp warp : warps) {
            final Location loc = warp.getLocation();
            if (useBlockloc && areCoordinatesEquals(loc, location) || loc.equals(location)) {
                return true;
            }
        }
        return false;
    }

    public boolean areCoordinatesEquals(Location primary, Location secondary) {
        return primary.getWorld() == secondary.getWorld() && primary.getX() == secondary.getX()
            && primary.getY() == secondary.getY() && primary.getZ() == secondary.getZ();

    }

    @NotNull public Collection<Warp> getWarps() {
        return new HashSet<>(warps);
    }

    @NotNull public Collection<Warp> getWarps(Predicate<Warp> filter) {
        return warps.stream().filter(filter).collect(Collectors.toSet());
    }

    @NotNull public Collection<Warp> getWarps(World world) {
        return getWarps((warp -> warp.getLocation().getWorld() == world));
    }

    @NotNull public Collection<Warp> getWarpsOwnedByPlayer(@NotNull final UUID player) {
        return getWarps(warp -> warp.getOwner() != null && warp.getOwner().equals(player));
    }

    public void registerWarp(@NotNull final Warp warp) {
        warps.remove(warp);
        warps.add(warp);
    }

    public void removeWarp(@NotNull final Warp warp) {
        warps.remove(warp);
    }
}
