package com.github.xiavic.essentials.Utils.warp;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class Warp {

    private final UUID uniqueID = UUID.randomUUID();

    private String name;
    private UUID owner;
    private Location location;

    public Warp(@NotNull final String name, @Nullable final UUID owner, final Location location) {
        this.name = name;
        this.owner = owner;
        this.location = location.clone();
    }

    public void setLocation(@NotNull final Location location) {
        this.location = location;
    }

    public void setName(@NotNull final String name) {
        this.name = name;
    }

    public void setOwner(@Nullable final UUID owner) {
        this.owner = owner;
    }

    @NotNull public String getName() {
        return name;
    }

    @Nullable public UUID getOwner() {
        return owner;
    }

    @NotNull public UUID getUniqueID() {
        return uniqueID;
    }

    public @NotNull Location getLocation() {
        return location.clone();
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
        if (!Objects.equals(owner, warp.owner))
            return false;
        return Objects.equals(location, warp.location);
    }

    @Override public int hashCode() {
        int result = uniqueID.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
