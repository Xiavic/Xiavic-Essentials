package com.github.xiavic.essentials.Commands.StaffCmds.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO: Refactor Utils
public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1) {
                if (player.hasPermission(Main.permissions.getString("HealOthers")) || player.isOp()) {
                    String who = strings[0];
                    if (who.equalsIgnoreCase("all")) {
                        for (Player target : Bukkit.getOnlinePlayers()) {
                            Utils.chat(target, Main.messages.getString("Heal"));
                            target.setHealth(target.getMaxHealth());
                            target.getActivePotionEffects().clear();
                        }
                        player.sendMessage(Utils.chat(Main.messages.getString("HealAll")));
                        return true;
                    } else {
                        try {
                            Player target = Bukkit.getPlayer(who);
                            Utils.chat(target, Main.messages.getString("Heal"));
                            target.setHealth(target.getMaxHealth());
                            target.getActivePotionEffects().clear();
                            Utils.chat(player, Main.messages.getString("HealOther").replace("%target%", target.getDisplayName()));
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
                if (player.hasPermission(Main.permissions.getString("Heal")) || player.isOp()) {
                    Utils.chat(player, Main.messages.getString("Heal"));
                    player.setHealth(player.getMaxHealth());
                    player.getActivePotionEffects().clear();
                } else {
                    Utils.chat(player, Main.messages.getString("NoPerms"));
                }
                return true;
            }
        } else {
            if (strings.length == 1) {
                String who = strings[0];
                if (who.equalsIgnoreCase("all")) {
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        Utils.chat(target, Main.messages.getString("Heal"));
                        target.setHealth(target.getMaxHealth());
                        target.getActivePotionEffects().clear();
                    }
                    commandSender.sendMessage(Utils.chat(Main.messages.getString("HealAll")));
                    return true;
                } else {
                    try {
                        Player target = Bukkit.getPlayer(who);
                        Utils.chat(target, Main.messages.getString("Heal"));
                        target.setHealth(target.getMaxHealth());
                        target.getActivePotionEffects().clear();
                        Utils.chat(commandSender, Main.messages.getString("HealOther").replace("%target%", target.getDisplayName()));
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
