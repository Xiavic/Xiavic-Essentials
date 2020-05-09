package com.github.xiavic.essentials.Commands.StaffCmds.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1) {
                if (player.hasPermission(Main.permissions.getString("ClearOthers")) || player.isOp()) {
                    try {
                        Player target = Bukkit.getPlayer(strings[0]);
                        clear(target);
                        Utils.chat(player, Main.messages.getString("ClearInventoryOther").replace("%target%", target.getDisplayName()));
                        return true;
                    } catch (Exception e) {
                        Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                        return true;
                    }
                }
            } else {
                if (player.hasPermission(Main.permissions.getString("Clear")) || player.isOp()) {
                    clear(player);
                } else {
                    Utils.chat(player, Main.messages.getString("NoPerms"));
                }
                return true;
            }
        } else {
            if (strings.length == 1) {
                try {
                    Player target = Bukkit.getPlayer(strings[0]);
                    clear(target);
                    Utils.chat(commandSender, Main.messages.getString("ClearInventoryOther").replace("%target%", target.getDisplayName()));
                    return true;
                } catch (Exception e) {
                    Utils.chat(commandSender, Main.messages.getString("PlayerNotFound"));
                    return true;
                }
            } else {
                commandSender.sendMessage("You need to specify a player to clear their inventory!");
            }
        }
        return false;
    }

    private void clear(Player player) {
        Inventory pInventory = player.getInventory();
        for (ItemStack item : pInventory.getStorageContents()) {
            pInventory.remove(item);
        }
        player.sendMessage(Utils.chat(Main.messages.getString("ClearInventory")));
    }

}
