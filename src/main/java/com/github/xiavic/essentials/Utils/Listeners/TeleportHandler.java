package com.github.xiavic.essentials.Utils.Listeners;

import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.TeleportationMessages;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TeleportHandler implements Listener, ITeleportHandler {

    private static final TeleportationMessages teleportationMessages =
            TeleportationMessages.INSTANCE;

    private final Collection<UUID> disabledPlayers = new HashSet<>();
    private final Map<UUID, Location> lastLocations = new HashMap<>();

    @Override
    public void processPlayerTeleport(Player player) {
        lastLocations.remove(player.getUniqueId());
        lastLocations.put(player.getUniqueId(), player.getLocation());
    }

    @Override
    public void processPlayerToggle(Player player) {
        if (disabledPlayers.contains(player.getUniqueId())) {
            disabledPlayers.remove(player.getUniqueId());
            Utils.sendMessage(player, teleportationMessages.messageTeleportToggleDisabled);
        } else {
            disabledPlayers.add(player.getUniqueId());
            Utils.sendMessage(player, teleportationMessages.messageTeleportToggleEnabled);
        }
    }

    @Override
    public CompletableFuture<Boolean> teleport(Player player, Location location) {
        processPlayerTeleport(player);
        return PaperLib.teleportAsync(player, location);
    }

    // change - if true: teleport player2 to player1 else teleport player1 to player2
    @Override
    public CompletableFuture<Boolean> teleport(Player p1, Player p2, boolean change) {
        if (change) {
            if (!disabledPlayers.contains(p1.getUniqueId())) {
                return teleport(p2, p1.getLocation());
            }
        }
        if (!disabledPlayers.contains(p2.getUniqueId())) {
            return teleport(p1, p2.getLocation());
        }
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public CompletableFuture<Boolean> remoteTp(Player player, Location location) {
        if (!disabledPlayers.contains(player.getUniqueId())) {
            return teleport(player, location).thenApply(result -> {
                if (!result) {
                    Utils.sendMessage(player, teleportationMessages.messageTeleportFailed);
                }
                return result;
            });
        }
        return CompletableFuture.completedFuture(true);
    }

    // 0 - teleport successful
    // 1 - player1 disabled
    // 2 - player2 disabled
    @Override
    public int remoteTp(Player p1, Player p2) {
        if (disabledPlayers.contains(p1.getUniqueId()))
            return 1;
        if (disabledPlayers.contains(p2.getUniqueId()))
            return 2;
        teleport(p1, p2.getLocation());
        return 0;
    }

    @Override
    public Location getLastLocation(Player player) {
        if (!lastLocations.containsKey(player.getUniqueId()))
            return null;
        return lastLocations.get(player.getUniqueId());
    }

    @Override
    public boolean isDisabled(Player player) {
        return disabledPlayers.contains(player.getUniqueId());
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        lastLocations.put(event.getEntity().getUniqueId(), event.getEntity().getLocation());
    }

}
