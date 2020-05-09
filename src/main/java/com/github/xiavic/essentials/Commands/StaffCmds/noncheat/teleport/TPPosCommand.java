package com.github.xiavic.essentials.Commands.StaffCmds.noncheat.teleport;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.TeleportHandler;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPPosCommand implements CommandExecutor {
    TeleportHandler teleportHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Tppos")) || player.isOp()) {
                if (args.length == 3 || args.length == 4) {
                    double x = Double.parseDouble(args[args.length - 3]);
                    double y = Double.parseDouble(args[args.length - 2]);
                    double z = Double.parseDouble(args[args.length - 1]);
                    if (args.length == 3) {
                        teleportHandler.teleport(player, new Location(player.getWorld(), x, y, z));
                    } else {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            teleportHandler.teleport(target, new Location(target.getWorld(), x, y, z));
                        } else {
                            Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
