package com.github.xiavic.essentials.Utils.warp;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a warp which would be used in /pw, /privatewarp
 */
public class PrivateWarp extends Warp {

    private UUID owner;
    private Collection<UUID> whitelisted = new HashSet<>();
    private Collection<UUID> blacklisted = new HashSet<>();

    public PrivateWarp(@NotNull final String name, @NotNull Location location,
        @NotNull final UUID owner) {
        super(name, location);
        this.owner = owner;
    }

    public PrivateWarp(@NotNull final String name, @NotNull Location location,
        @NotNull final UUID owner, @Nullable final Collection<UUID> whitelisted,
        @Nullable final Collection<UUID> blacklisted) {
        this(name, location, owner);
        if (whitelisted != null) {
            this.whitelisted = new HashSet<>(whitelisted);
        }
        if (blacklisted != null) {
            this.blacklisted = new HashSet<>(blacklisted);
        }
    }

    @NotNull
    public Collection<UUID> getWhitelisted() {
        return new HashSet<>(whitelisted);
    }

    @NotNull
    public Collection<UUID> getBlacklisted() {
        return new HashSet<>(blacklisted);
    }

    public boolean isWhitelisted(final UUID uuid) {
        return whitelisted.contains(uuid);
    }

    public boolean isBlacklisted(final UUID uuid) {
        return blacklisted.contains(uuid);
    }

    @NotNull
    public PrivateWarp setOwner(final UUID owner) {
        this.owner = owner;
        return this;
    }

    @NotNull
    public UUID getOwner() {
        return this.owner;
    }

    /**
     * Adds a player to the whitelist and if the player is
     * blacklisted, they are removed from the blacklist.
     *
     * @param player The UniqueID of the player.
     */
    public void addToWhitelist(final UUID player) {
        whitelisted.remove(player);
        whitelisted.add(player);
        blacklisted.remove(player);
    }

    /**
     * Adds a player to the blacklist and if the player is
     * blacklisted, they are removed from the whitelist.
     *
     * @param player The UniqueID of the player.
     */
    public void addToBlacklist(final UUID player) {
        blacklisted.remove(player);
        blacklisted.add(player);
        whitelisted.remove(player);
    }

    public void removeFromWhitelist(final UUID player) {
        whitelisted.remove(player);
    }

    public void removeFromBlacklist(final UUID player) {
        blacklisted.remove(player);
    }

    @Override public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        PrivateWarp that = (PrivateWarp) o;

        if (!Objects.equals(whitelisted, that.whitelisted))
            return false;
        if (!Objects.equals(owner, that.owner)) {
            return false;
        }
        return Objects.equals(blacklisted, that.blacklisted);
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (whitelisted != null ? whitelisted.hashCode() : 0);
        result = 31 * result + (blacklisted != null ? blacklisted.hashCode() : 0);
        result = 31 * result + owner.hashCode();
        return result;
    }
}
