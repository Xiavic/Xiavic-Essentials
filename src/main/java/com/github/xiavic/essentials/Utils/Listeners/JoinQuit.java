package com.github.xiavic.essentials.Utils.Listeners;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.LocationUtils;
import com.github.xiavic.essentials.Utils.Misc.everythingElse;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinQuit implements Listener {

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String name = p.getDisplayName();
        if (!p.hasPlayedBefore()) {
            event.setJoinMessage(
                Utils.chat(Main.messages.getString("FirstJoin_Message").replace("%player%", name)));
            Utils.chat(p, "&6Welcome " + p.getName());
            if (!p.isOp()) {
                p.teleport(LocationUtils.getLocation("FirstSpawn"));
                // This is setup in place for ops who are first installing the plugin, and don't want to lose their spot to Setspawn. ( They wont be teleported )
            }
        } else {
            event.setJoinMessage(Utils.chat(Main.messages.getString("ReJoin_Message").replace("%player%", name)));
            if (everythingElse.isFrozen(p)) {
                Utils.chat(p, Main.messages.getString("FrozenLogin"));
            }
        }

    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        String name = p.getDisplayName();
        event.setQuitMessage(Utils.chat(Main.messages.getString("Quit_Message").replace("%player%", name)));
    }
}
