package com.github.xiavic.essentials.Commands.staff.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1) {
                if (player.hasPermission(Main.permissions.getString("FeedOthers")) || player.isOp()) {
                    String who = strings[0];
                    if (who.equalsIgnoreCase("all")) {
                        for (Player target : Bukkit.getOnlinePlayers()) {
                            Utils.chat(target, Main.messages.getString("Feed"));
                            target.setFoodLevel(20);
                            target.setSaturation(20f);
                        }
                        Utils.chat(player, Main.messages.getString("FeedAll"));
                        return true;
                    } else {
                        try {
                            Player target = Bukkit.getPlayer(who);
                            Utils.chat(target, Main.messages.getString("Feed"));
                            target.setFoodLevel(20);
                            target.setSaturation(20f);
                            player.sendMessage(who + " has been fed!");
                            return true;
                        } catch (Exception e) {
                            Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                            return true;
                        }
                    }
                } else {
                    Utils.chat(player, Main.messages.getString("NoPerms"));
                    return true;
                }
            } else {
                if (player.hasPermission(Main.permissions.getString("Feed")) || player.isOp()) {
                    Utils.chat(player, Main.messages.getString("Feed"));
                    player.setFoodLevel(20);
                    player.setSaturation(20f);
                } else {
                    Utils.chat(player, Main.messages.getString("NoPerms"));
                }
                return true;
            }
        } else {
            if (strings.length == 1) {
                String who = strings[0];
                if (who.equalsIgnoreCase("all")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Utils.chat(player, Main.messages.getString("Feed"));
                        player.setFoodLevel(20);
                        player.setSaturation(20f);
                    }
                    Utils.chat(commandSender, Main.messages.getString("FeedAll"));
                    return true;
                } else {
                    try {
                        Player player = Bukkit.getPlayer(who);
                        Utils.chat(player, Main.messages.getString("Feed"));
                        player.setFoodLevel(20);
                        player.setSaturation(20f);
                        Utils.chat(commandSender, player.getDisplayName() + " has been fed!");
                        return true;
                    } catch (Exception e) {
                        Utils.chat(commandSender, Main.messages.getString("PlayerNotFound"));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
