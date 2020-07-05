package com.github.xiavic.essentials.Utils.warp;

import com.github.xiavic.essentials.Utils.Lockable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a warp which would be used in /pw, /privatewarp
 */
public class PrivateWarp extends Warp implements Lockable {
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

    public PrivateWarp (@NotNull final PrivateWarp privateWarp) {
        super(privateWarp);
        this.whitelisted = new HashSet<>(privateWarp.whitelisted);
        this.blacklisted = privateWarp.blacklisted;
    }

    @NotNull
    public Collection<UUID> getWhitelisted() {
        lock();
        final Collection<UUID> collection = new ArrayList<>(this.whitelisted);
        unlock();
        return collection;
    }

    @NotNull
    public Collection<UUID> getBlacklisted() {
        lock();
        final Collection<UUID> collection = new ArrayList<>(blacklisted);
        unlock();
        return collection;
    }

    public boolean isWhitelisted(final UUID uuid) {
        return whitelisted.contains(uuid);
    }

    public boolean isBlacklisted(final UUID uuid) {
        lock();
        boolean value = blacklisted.contains(uuid);
        unlock();
        return value;
    }

    @NotNull
    public UUID getOwner() {
        return this.owner;
    }

    @NotNull
    public PrivateWarp setOwner(final UUID owner) {
        lock();
        this.owner = owner;
        unlock();
        return this;
    }

    /**
     * Adds a player to the whitelist and if the player is
     * blacklisted, they are removed from the blacklist.
     *
     * @param player The UniqueID of the player.
     */
    public void addToWhitelist(final UUID player) {
        lock();
        whitelisted.remove(player);
        whitelisted.add(player);
        blacklisted.remove(player);
        unlock();
    }

    @Override
    public boolean canBeAccessedBy(final Permissible permissible) {
        if (permissible instanceof Entity) {
            lock();
            Entity entity = (Entity) permissible;
            final UUID uuid = entity.getUniqueId();
            boolean bool = !isBlacklisted(uuid);
            bool = isEnabled() && whitelisted.isEmpty() ? bool : isWhitelisted(uuid);
            unlock();
            return bool;
        }
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean hasPermission() {
        return false;
    }

    @Override
    public Warp setPermission(final @Nullable String permission) {
        return this;
    }

    /**
     * Adds a player to the blacklist and if the player is
     * blacklisted, they are removed from the whitelist.
     *
     * @param player The UniqueID of the player.
     */
    public void addToBlacklist(final UUID player) {
        lock();
        blacklisted.remove(player);
        blacklisted.add(player);
        whitelisted.remove(player);
        unlock();
    }

    public void removeFromWhitelist(final UUID player) {
        lock();
        whitelisted.remove(player);
        unlock();
    }

    public void removeFromBlacklist(final UUID player) {
        lock();
        blacklisted.remove(player);
        unlock();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        PrivateWarp that = (PrivateWarp) o;
        lock();
        that.lock();
        if (!Objects.equals(whitelisted, that.whitelisted)) {
            unlock();
            that.unlock();
            return false;
        }
        if (!Objects.equals(owner, that.owner)) {
            unlock();
            that.unlock();
            return false;
        }
        final boolean value = Objects.equals(blacklisted, that.blacklisted);
        unlock();
        return value;
    }

    @Override
    public int hashCode() {
        lock();
        int result = super.hashCode();
        result = 31 * result + (whitelisted != null ? whitelisted.hashCode() : 0);
        result = 31 * result + (blacklisted != null ? blacklisted.hashCode() : 0);
        result = 31 * result + owner.hashCode();
        unlock();
        return result;
    }
}
