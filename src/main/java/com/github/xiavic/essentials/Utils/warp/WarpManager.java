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

    public Optional<Warp> getWarp(final String name) {
        return getWarps(warp -> warp.getName().equalsIgnoreCase(name)).findAny();
    }

    public <T extends Warp> Optional<T> getWarp(final String name, final Class<T> clazz, Predicate<T> filter) {
        return getWarps(clazz).filter(warp -> warp.getName().equalsIgnoreCase(name) && filter.test(warp)).findAny();
    }

    public <T extends Warp> Optional<T> getWarp(final String name, final Class<T> clazz) {
        return getWarps(clazz).filter(warp -> warp.getName().equalsIgnoreCase(name)).findAny();
    }

    public Optional<Warp> getBaseWarp(final String name) {
        return getWarps(Warp::isBaseWarp).filter(warp -> warp.getName().equalsIgnoreCase(name))
            .findAny();
    }

    public <T extends Warp> Stream<T> getWarps(@NotNull final Class<T> clazz) {
        return getWarps(clazz::isInstance).map(clazz::cast);
    }

    @NotNull public Collection<Warp> getWarps() {
        return new HashSet<>(warps);
    }

    @NotNull public Stream<Warp> getWarps(@NotNull final Predicate<Warp> filter) {
        return warps.stream().filter(filter);
    }

    @NotNull public Collection<Warp> getFilteredWarps(@NotNull final Predicate<Warp> filter) {
        return getWarps(filter).collect(Collectors.toSet());
    }

    @NotNull public Collection<Warp> getWarps(World world) {
        return getFilteredWarps(warp -> warp.getLocation().getWorld() == world);
    }

    @NotNull
    public Collection<PrivateWarp> getPrivateWarpsOwnedByPlayer(@NotNull final UUID player) {
        return getWarps(PrivateWarp.class::isInstance).map(PrivateWarp.class::cast)
            .filter(privateWarp -> privateWarp.getOwner().equals(player))
            .collect(Collectors.toSet());
    }

    @NotNull
    public Collection<Warp> getAccessibleToPermissible(@NotNull final Permissible permissible) {
        return getFilteredWarps(warp -> warp.canBeAccessedBy(permissible));
    }

    public <T> Collection<T> getAccessibleToPermissible(@NotNull final Permissible permissible, Class<T> clazz) {
        return getWarps(clazz::isInstance).map(clazz::cast).filter(warp -> ((Warp) warp).canBeAccessedBy(permissible)).collect(
            Collectors.toList());
    }

    public void registerWarp(@NotNull final Warp warp) {
        warps.remove(warp);
        warps.add(warp);
    }

    public void unregisterWarp(@NotNull final Warp warp) {
        warps.remove(warp);
    }
}
