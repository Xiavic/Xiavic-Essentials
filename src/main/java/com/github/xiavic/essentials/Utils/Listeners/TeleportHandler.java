package com.github.xiavic.essentials.Utils.Listeners;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TeleportHandler implements Listener, ITeleportHandler {

    private List<Player> disabledPlayers = new ArrayList<>();
    private Map<Player, Location> lastLocations = new HashMap<>();

    @Override public void processPlayerTeleport(Player player) {
        lastLocations.put(player, player.getLocation());
    }

    @Override public void processPlayerToggle(Player player) {
        if (disabledPlayers.contains(player)) {
            disabledPlayers.remove(player);
            Utils.chat(player, Main.messages.getString("TpToggleOff"));
        } else {
            disabledPlayers.add(player);
            Utils.chat(player, Main.messages.getString("TpToggleOn"));
        }
    }

    @Override public void teleport(Player player, Location location) {
        processPlayerTeleport(player);
        player.teleport(location);
    }

    // change - if true: teleport player2 to player1 else teleport player1 to player2
    @Override public boolean teleport(Player p1, Player p2, boolean change) {
        if (change) {
            if (!disabledPlayers.contains(p1)) {
                teleport(p2, p1.getLocation());
                return true;
            }
        }
        if (!disabledPlayers.contains(p2)) {
            teleport(p1, p2.getLocation());
            return true;
        }
        return false;
    }

    @Override public boolean remoteTp(Player player, Location location) {
        if (!disabledPlayers.contains(player)) {
            teleport(player, location);
            return true;
        }
        return true;
    }

    // 0 - teleport successful
    // 1 - player1 disabled
    // 2 - player2 disabled
    @Override public int remoteTp(Player p1, Player p2) {
        if (disabledPlayers.contains(p1))
            return 1;
        if (disabledPlayers.contains(p2))
            return 2;
        teleport(p1, p2.getLocation());
        return 0;
    }

    @Override public Location getLastLocation(Player player) throws Exception {
        if (!lastLocations.containsKey(player))
            throw new Exception();
        return lastLocations.get(player);
    }

    @Override public boolean isDisabled(Player player) {
        return disabledPlayers.contains(player);
    }

    @EventHandler public void onDeath(PlayerDeathEvent e) {
        lastLocations.put(e.getEntity(), e.getEntity().getLocation());
    }

}
