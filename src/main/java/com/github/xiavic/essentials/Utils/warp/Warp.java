package com.github.xiavic.essentials.Utils.warp;

import com.github.xiavic.essentials.Utils.Lockable;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Warp implements Lockable {

    private final UUID uniqueID;
    private transient volatile Thread locker;
    private boolean enabled;

    private String name;
    private String permission;
    private Location location;

    public Warp(@NotNull final String name, final Location location) {
        this.uniqueID = UUID.randomUUID();
        this.name = name;
        this.location = location.clone();
    }

    public Warp(@NotNull final Warp warp) {
        this.name = warp.name;
        this.permission = warp.permission;
        this.location = warp.location;
        this.enabled = warp.enabled;
        this.uniqueID = warp.uniqueID;
    }

    public final boolean isBaseWarp() {
        return this.getClass() == Warp.class;
    }

    @NotNull
    public String getName() {
        lock();
        final String name = this.name;
        unlock();
        return name;
    }

    public Warp setName(@NotNull final String name) {
        lock();
        this.name = name;
        unlock();
        return this;
    }

    @NotNull
    public UUID getUniqueID() {
        return uniqueID;
    }

    public boolean hasPermission() {
        lock();
        boolean result = permission != null && !permission.isEmpty();
        unlock();
        return result;
    }

    public String getPermission() {
        lock();
        final String permission = this.permission;
        unlock();
        return permission;
    }

    public Warp setPermission(@Nullable final String permission) {
        lock();
        this.permission = permission;
        unlock();
        return this;
    }

    public @NotNull Location getLocation() {
        lock();
        final Location cloned = this.location.clone();
        unlock();
        return cloned;
    }

    public Warp setLocation(@NotNull final Location location) {
        lock();
        this.location = location;
        unlock();
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Warp setEnabled(final boolean enabled) {
        lock();
        this.enabled = enabled;
        unlock();
        return this;
    }

    public boolean canBeAccessedBy(final Permissible permissible) {
        return !hasPermission() || permissible.hasPermission(permission);
    }

    public CompletableFuture<Boolean> teleport(final Entity entity) {
        if (!canBeAccessedBy(entity) || !isEnabled()) {
            return CompletableFuture.completedFuture(false);
        }
        return PaperLib.teleportAsync(entity, location);
    }

    @Override
    public boolean isLocked() {
        return locker == null;
    }

    @Override
    public Thread getLocker() {
        return locker;
    }

    @Override
    public void lock() {
        while (isLocked())
            ;
        this.locker = Thread.currentThread();
    }

    @Override
    public void unlock() {
        if (!isLocked() || locker != Thread.currentThread()) {
            return;
        }
        this.locker = null;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Warp warp = (Warp) o;
        lock();
        warp.lock();
        if (!Objects.equals(uniqueID, warp.uniqueID)) {
            unlock();
            warp.unlock();
            return false;
        }
        if (!Objects.equals(name, warp.name)) {
            unlock();
            warp.unlock();
            return false;
        }
        if (!Objects.equals(permission, warp.permission)) {
            unlock();
            warp.unlock();
            return false;
        }
        boolean value = Objects.equals(location, warp.location);
        unlock();
        warp.unlock();
        return value;
    }

    @Override
    public int hashCode() {
        lock();
        int result = uniqueID.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        unlock();
        return result;
    }
}
