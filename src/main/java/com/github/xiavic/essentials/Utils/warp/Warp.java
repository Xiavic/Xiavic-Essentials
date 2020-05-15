package com.github.xiavic.essentials.Utils.warp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class Warp {

    private final UUID uniqueID = UUID.randomUUID();
    private boolean enabled;

    private String name;
    private String permission;
    private Location location;

    public Warp(@NotNull final String name, final Location location) {
        this.name = name;
        this.location = location.clone();
    }

    public Warp setPermission(@Nullable final String permission) {
        this.permission = permission;
        return this;
    }

    public Warp setLocation(@NotNull final Location location) {
        this.location = location;
        return this;
    }

    public Warp setName(@NotNull final String name) {
        this.name = name;
        return this;
    }

    public Warp setEnabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @NotNull public String getName() {
        return name;
    }

    @NotNull public UUID getUniqueID() {
        return uniqueID;
    }

    public boolean hasPermission() {
        return permission != null && !permission.isEmpty();
    }

    public @NotNull Location getLocation() {
        return location.clone();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean canBeAccessedBy(final Entity entity) {
        return !hasPermission() || entity.hasPermission(permission);
    }

    public boolean teleport(final Entity entity) {
        return canBeAccessedBy(entity) && isEnabled() && entity.teleport(location);
    }


    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Warp warp = (Warp) o;

        if (!Objects.equals(uniqueID, warp.uniqueID))
            return false;
        if (!Objects.equals(name, warp.name))
            return false;
        if (!Objects.equals(permission, warp.permission))
            return false;
        return Objects.equals(location, warp.location);
    }

    @Override public int hashCode() {
        int result = uniqueID.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
