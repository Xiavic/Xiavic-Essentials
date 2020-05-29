package com.github.xiavic.essentials.Commands.player.Essential;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExtinguishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {
            if (player.hasPermission(Main.permissions.getString("Extinguish")) || player.isOp()) {
                player.setFireTicks(0);
                Utils.chat(player, Main.messages.getString("Extinguish"));
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            if (args.length == 1) {
                if (player.hasPermission(Main.permissions.getString("ExtinguishOthers"))) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target.isOnline()) {
                        target.setFireTicks(0);
                        Utils.chat(target, Main.messages.getString("ExtinguishTarget").replace("%sender%", player.getDisplayName()));
                        return true;
                    }
                } else {
                    Utils.chat(player, Main.messages.getString("NoPerms"));
                }
            } else {
                Utils.chat(player, Main.messages.getString("PlayerNotFound"));
            }
        }
        return false;
    }
}
