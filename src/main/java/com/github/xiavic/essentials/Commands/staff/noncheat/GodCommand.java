package com.github.xiavic.essentials.Commands.staff.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO: Refactor Utils
public class GodCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            if (player.hasPermission(Main.permissions.getString("GodOthers")) || player.isOp()) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (!target.isInvulnerable()) {
                        target.setInvulnerable(true);
                        Utils.chat(target, Main.messages.getString("God").replace("%mode%", Main.messages.getString("Enabled")));
                        Utils.chat(player, Main.messages.getString("GodOthers").replace("%mode%", Main.messages.getString("Enabled")).replace("%player%", target.getDisplayName()));
                    } else if (target.isInvulnerable()) {
                        target.setInvulnerable(false);
                        Utils.chat(target, Main.messages.getString("God").replace("%mode%", Main.messages.getString("Disabled")));
                        Utils.chat(player, Main.messages.getString("GodOthers").replace("%mode%", Main.messages.getString("Disabled")).replace("%player%", target.getDisplayName()));
                    }
                    return true;
                } else {
                    player.sendMessage(Utils.chat(Main.messages.getString("PlayerNotFound")));
                }
            } else {
                player.sendMessage(Utils.chat(Main.messages.getString("NoPerms")));
            }
        } else {
            if (player.hasPermission(Main.permissions.getString("God")) || player.isOp()) {
                if (!player.isInvulnerable()) {
                    player.setInvulnerable(true);
                    Utils.chat(player, Main.messages.getString("God").replace("%mode%", Main.messages.getString("Enabled")));
                } else if (player.isInvulnerable()) {
                    player.setInvulnerable(false);
                    Utils.chat(player, Main.messages.getString("God").replace("%mode%", Main.messages.getString("Disabled")));
                }
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        }
        return false;
    }
}
