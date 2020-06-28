package com.github.xiavic.essentials.Commands.staff.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

// TODO: Refactor Utils
public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
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
                            target.removePotionEffect(PotionEffectType.BLINDNESS);
                            target.removePotionEffect(PotionEffectType.BAD_OMEN);
                            target.removePotionEffect(PotionEffectType.HUNGER);
                            target.removePotionEffect(PotionEffectType.CONFUSION);
                            target.removePotionEffect(PotionEffectType.POISON);
                            target.removePotionEffect(PotionEffectType.UNLUCK);
                            target.removePotionEffect(PotionEffectType.WEAKNESS);
                            target.removePotionEffect(PotionEffectType.WITHER);

                        }
                        player.sendMessage(Utils.chat(Main.messages.getString("HealAll")));
                        return true;
                    } else {
                        try {
                            Player target = Bukkit.getPlayer(who);
                            Utils.chat(target, Main.messages.getString("Heal"));
                            target.setHealth(target.getMaxHealth());
                            target.getActivePotionEffects().clear();
                            target.removePotionEffect(PotionEffectType.BLINDNESS);
                            target.removePotionEffect(PotionEffectType.BAD_OMEN);
                            target.removePotionEffect(PotionEffectType.HUNGER);
                            target.removePotionEffect(PotionEffectType.CONFUSION);
                            target.removePotionEffect(PotionEffectType.POISON);
                            target.removePotionEffect(PotionEffectType.UNLUCK);
                            target.removePotionEffect(PotionEffectType.WEAKNESS);
                            target.removePotionEffect(PotionEffectType.WITHER);
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
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.removePotionEffect(PotionEffectType.BAD_OMEN);
                    player.removePotionEffect(PotionEffectType.HUNGER);
                    player.removePotionEffect(PotionEffectType.CONFUSION);
                    player.removePotionEffect(PotionEffectType.POISON);
                    player.removePotionEffect(PotionEffectType.UNLUCK);
                    player.removePotionEffect(PotionEffectType.WEAKNESS);
                    player.removePotionEffect(PotionEffectType.WITHER);
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
                        target.removePotionEffect(PotionEffectType.BLINDNESS);
                        target.removePotionEffect(PotionEffectType.BAD_OMEN);
                        target.removePotionEffect(PotionEffectType.HUNGER);
                        target.removePotionEffect(PotionEffectType.CONFUSION);
                        target.removePotionEffect(PotionEffectType.POISON);
                        target.removePotionEffect(PotionEffectType.UNLUCK);
                        target.removePotionEffect(PotionEffectType.WEAKNESS);
                        target.removePotionEffect(PotionEffectType.WITHER);
                    }
                    commandSender.sendMessage(Utils.chat(Main.messages.getString("HealAll")));
                    return true;
                } else {
                    try {
                        Player target = Bukkit.getPlayer(who);
                        Utils.chat(target, Main.messages.getString("Heal"));
                        target.setHealth(target.getMaxHealth());
                        target.getActivePotionEffects().clear();
                        target.removePotionEffect(PotionEffectType.BLINDNESS);
                        target.removePotionEffect(PotionEffectType.BAD_OMEN);
                        target.removePotionEffect(PotionEffectType.HUNGER);
                        target.removePotionEffect(PotionEffectType.CONFUSION);
                        target.removePotionEffect(PotionEffectType.POISON);
                        target.removePotionEffect(PotionEffectType.UNLUCK);
                        target.removePotionEffect(PotionEffectType.WEAKNESS);
                        target.removePotionEffect(PotionEffectType.WITHER);
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
