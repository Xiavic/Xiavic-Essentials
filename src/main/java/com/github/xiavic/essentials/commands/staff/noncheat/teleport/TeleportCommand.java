package com.github.xiavic.essentials.commands.staff.noncheat.teleport;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.TeleportHandler;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
    TeleportHandler teleportHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("TP")) || player.isOp()) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!teleportHandler.teleport(player, target, false))
                            Utils.chat(player, Main.messages.getString("TpDisabled"));
                    } else {
                        Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                    }
                } else if (args.length == 2) {
                    if (player.hasPermission(Main.permissions.getString("TPOthers")) || player.isOp()) {
                        Player target = Bukkit.getPlayer(args[0]);
                        Player target2 = Bukkit.getPlayer(args[1]);
                        if (target != null && target2 != null) {
                            int result = teleportHandler.remoteTp(target, target2);
                            switch (result) {
                                case 0:
                                    Utils.chat(player, Main.messages.getString("TpRemote")
                                            .replace("%target1%", target.getDisplayName())
                                            .replace("%target2%", target2.getDisplayName()));
                                    break;
                                case 1:
                                    Utils.chat(player, Main.messages.getString("%target%")
                                            .replace("%target%", target.getDisplayName()));
                                    break;
                                case 2:
                                    Utils.chat(player, Main.messages.getString("%target%")
                                            .replace("%target%", target2.getDisplayName()));
                                    break;
                            }
                        } else {
                            Utils.chat(player, Main.messages.getString("PlayerNotFound2"));
                            return true;
                        }
                    } else {
                        Utils.chat(player, Main.messages.getString("NoPerms"));
                    }
                } else {
                    Utils.chat(player, Main.messages.getString("SpecifyTarget"));

                }
                return true;
            } else {
                Utils.chat(sender, Main.messages.getString("NoPerms"));
            }

        }
        sender.sendMessage(Utils.chat(Main.messages.getString("SenderNotPlayer")));
        return false;
    }
}
