package com.github.xiavic.essentials.Utils.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IWarpManager<W extends Warp> {


    static boolean areCoordinatesEquals(Location primary, Location secondary) {
        return primary.getWorld() == secondary.getWorld() && primary.getX() == secondary.getX()
                && primary.getY() == secondary.getY() && primary.getZ() == secondary.getZ();
    }

    boolean isWarp(@NotNull Location location, boolean useBlockloc);

    Optional<W> getWarp(String name);

    @NotNull Collection<W> getWarps();

    @NotNull Stream<W> getWarps(@NotNull Predicate<W> filter);

    @NotNull Collection<W> getFilteredWarps(@NotNull Predicate<W> filter);

    @NotNull Collection<W> getWarps(World world);

    @NotNull Collection<W> getAccessibleToPermissible(@NotNull Permissible permissible);

    void registerWarp(@NotNull W warp);

    void unregisterWarp(@NotNull W warp);
}
