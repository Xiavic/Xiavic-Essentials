package com.github.xiavic.essentials.commands.staff.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            if (player.hasPermission(Main.permissions.getString("FlyOthers")) || player.isOp()) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (target.getAllowFlight()) {
                        target.setAllowFlight(false);
                        Utils.chat(target, Main.messages.getString("Fly").replace("%mode%", Main.messages.getString("Disabled")));
                        Utils.chat(player, Main.messages.getString("FlyOthers").replace("%target%", target.getDisplayName()).replace("%mode%", Main.messages.getString("Disabled")));
                    } else if (!target.getAllowFlight()) {
                        target.setAllowFlight(true);
                        Utils.chat(target, Main.messages.getString("Fly").replace("%mode%", Main.messages.getString("Enabled")));
                        Utils.chat(player, Main.messages.getString("FlyOthers").replace("%target%", target.getDisplayName()).replace("%mode%", Main.messages.getString("Enabled")));
                    }
                    return true;
                } else {
                    Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                }
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            if (player.hasPermission(Main.permissions.getString("Fly")) || player.isOp()) {
                if (!player.getAllowFlight()) {
                    player.setAllowFlight(true);
                    Utils.chat(player, Main.messages.getString("Fly").replace("%mode%", Main.messages.getString("Enabled")));
                } else if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    Utils.chat(player, Main.messages.getString("Fly").replace("%mode%", Main.messages.getString("Disabled")));
                }
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        }
        return false;
    }
}
