package com.github.xiavic.essentials.Utils.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PrivateWarpManager implements IWarpManager<PrivateWarp> {

    INSTANCE;

    private @NotNull Collection<PrivateWarp> warps = new HashSet<>();

    @Override public boolean isWarp(@NotNull final Location location, final boolean useBlockloc) {
        return warps.stream()
            .anyMatch(warp -> IWarpManager.areCoordinatesEquals(warp.getLocation(), location));
    }

    @Override public Optional<PrivateWarp> getWarp(@NotNull final String name) {
        return warps.stream().filter((PrivateWarp warp) -> warp.getName().equalsIgnoreCase(name))
            .findAny();
    }

    @NotNull
    public Optional<PrivateWarp> getWarp(@NotNull final UUID owner, @NotNull final String name) {
        return warps.stream().filter(
            (PrivateWarp warp) -> warp.getOwner().equals(owner) && warp.getName()
                .equalsIgnoreCase(name)).findAny();
    }

    @Override @NotNull public Collection<PrivateWarp> getWarps() {
        return new HashSet<>(warps);
    }

    @Override
    public @NotNull Stream<PrivateWarp> getWarps(@NotNull final Predicate<PrivateWarp> filter) {
        return warps.stream().filter(filter);
    }

    @Override @NotNull
    public Collection<PrivateWarp> getFilteredWarps(@NotNull final Predicate<PrivateWarp> filter) {
        return getWarps(filter).collect(Collectors.toSet());
    }

    @Override @NotNull public Collection<PrivateWarp> getWarps(final World world) {
        return getWarps(warp -> warp.getLocation().getWorld() == world).collect(Collectors.toSet());
    }

    @Override @NotNull public Collection<PrivateWarp> getAccessibleToPermissible(
        @NotNull final Permissible permissible) {
        return getWarps(warp -> warp.canBeAccessedBy(permissible)).collect(Collectors.toSet());
    }

    @Override public void registerWarp(@NotNull final PrivateWarp warp) {
        unregisterWarp(warp);
        warps.add(warp);
    }

    @Override public void unregisterWarp(@NotNull final PrivateWarp warp) {
        warps.remove(warp);
    }
}
