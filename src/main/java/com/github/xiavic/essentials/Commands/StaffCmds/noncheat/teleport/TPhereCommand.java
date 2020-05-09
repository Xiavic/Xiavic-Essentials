package com.github.xiavic.essentials.Commands.StaffCmds.noncheat.teleport;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.TeleportHandler;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPhereCommand implements CommandExecutor {
    TeleportHandler teleportHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Tphere")) || player.isOp()) {
                if (label.equalsIgnoreCase("tphere")) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        teleportHandler.teleport(player, target, true);
                    } else {
                        player.sendMessage(Utils.chat(Main.messages.getString("PlayerNotFound")));
                    }
                    return true;
                }
                return true;
            }
        } else {
            sender.sendMessage(Utils.chat(Main.messages.getString("NoPerms")));
        }
        return false;
    }
}
