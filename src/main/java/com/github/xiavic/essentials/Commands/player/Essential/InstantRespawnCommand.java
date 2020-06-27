package com.github.xiavic.essentials.Commands.player.Essential;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InstantRespawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean enabler = false;
            if (player.hasPermission(Main.permissions.getString("InstantRespawn")) || player.isOp()) {
                if (enabler) {
                    Utils.chat(player, Main.messages.getString("InstantRespawnToggle").replace("%mode%", Main.messages.getString("Enabled")));
                    if (player.isDead()) {
                        player.spigot().respawn();
                        return true;
                    }
                } else {
                    Utils.chat(player, Main.messages.getString("InstantRespawnToggle").replace("%mode%", Main.messages.getString("Disabled")));
                }
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
