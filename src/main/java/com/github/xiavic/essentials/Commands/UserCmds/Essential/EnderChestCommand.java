package com.github.xiavic.essentials.Commands.UserCmds.Essential;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class EnderChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length >= 1) {
                if (player.hasPermission(Main.permissions.getString("EnderChestOthers")) || player.isOp()) {
                    try {
                        Player target = Bukkit.getPlayer(strings[0]);
                        player.openInventory(target.getEnderChest());
                        Utils.chat(player, Main.messages.getString("EnderChestOthers").replace("%target%", target.getDisplayName()));
                        return true;
                    } catch (Exception e) {
                        Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                        return true;
                    }
                }
            } else {
                if (player.hasPermission(Main.permissions.getString("EnderChest")) || player.isOp()) {
                    player.openInventory(player.getEnderChest());
                    Utils.chat(player, Main.messages.getString("EnderChest"));
                    return true;
                }
            }
        } else {
            Utils.chat(commandSender, Main.messages.getString("SenderNotPlayer"));
            return true;
        }
        return false;
    }
}
