package com.github.xiavic.essentials.Utils.Listeners;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.github.xiavic.essentials.Main.messages;

public enum AFKHandler implements Listener {

    INSTANCE;

    private Map<UUID, Long> tickCount = new HashMap<>();
    private Collection<UUID> notAFK = new HashSet<>();
    private BukkitTask current;
    private long timeoutTicks;

    public void registerTicker() {
        if (current != null && !current.isCancelled()) {
            return;
        }
        current = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::tick, 0, 20);
    }

    public void clearTicker() {
        if (current != null && !current.isCancelled()) {
            tickCount.clear();
            notAFK.clear();
            current.cancel();
            current = null;
        }
    }

    public long getTimeoutTicks() {
        return timeoutTicks;
    }

    public void setTimeoutTicks(final long timeoutTicks) {
        if (timeoutTicks < 1) {
            throw new IllegalArgumentException("Ticks must be positive and > 1!");
        }
        this.timeoutTicks = timeoutTicks;
    }

    public void setTimeout(final long timeout, final @NotNull TimeUnit timeUnit) {
        setTimeoutTicks(Utils.toTicks(timeout, timeUnit));
    }

    public void tick() {
        tickCount.entrySet().removeIf(entry -> {
            entry.setValue(entry.getValue() - 1);
            if (entry.getValue() < 0) {
                notAFK.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }

    public void reset(final UUID player) {
        setAFK(player, false);
    }

    public boolean isAFK(final UUID player) {
        return !notAFK.contains(player);
    }

    public void setAFK(final UUID player, final boolean afk) { //TODO add broadcast
        final boolean currentlyAFK = isAFK(player);
        tickCount.remove(player);
        final Player playerObj = Bukkit.getPlayer(player);
        String messageKey;
        if (!currentlyAFK && afk) { //Becomes afk
            notAFK.remove(player);
            messageKey = "AFK";
        } else if (currentlyAFK && !afk) { //Become NOT afk
            notAFK.add(player);
            tickCount.put(player, timeoutTicks);
            messageKey = "NotAFK";
        } else {
            messageKey = null;
        }
        if (playerObj != null && messageKey != null) {
            Utils.chat(playerObj,
                messages.getString(messageKey).replace("%player%", playerObj.getDisplayName()));
        }
    }

    private void clearReferences(final UUID player) {
        notAFK.remove(player);
        tickCount.remove(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent event) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            if (event.getMessage().startsWith("/afk")) {
                return;
            }
            reset(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(final PlayerMoveEvent event) {
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(final PlayerInteractEvent event) {
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryOpen(final InventoryEvent event) {
        reset(event.getView().getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(final PlayerTeleportEvent event) {
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    //False because it shows the player attempted to do something.
    public void onCommandSender(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase()
            .startsWith("afk")) { //If it is the afk command - ignore.
            return;
        }
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEat(final PlayerRespawnEvent event) {
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemUse(final PlayerItemConsumeEvent event) {
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRespawn(final PlayerRespawnEvent event) {
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent event) {
        reset(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDisconnect(final PlayerQuitEvent event) {
        clearReferences(event.getPlayer().getUniqueId());
    }

}
