package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Map;

public class ItemInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("ItemInfo")) || player.isOp()) {
                if (!player.getInventory().getItemInMainHand().getType().isAir()) {
                    Utils.chat(player, " ");
                    if (player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                        Utils.chat(player, "&eItem Meta: &b" + player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                    }
                    Utils.chat(player, "&eMinecraft Name: &b" + player.getInventory().getItemInMainHand().getType().name());
                    Utils.chat(player, "&eMax Stack: &b" + player.getInventory().getItemInMainHand().getMaxStackSize());
                    if (!player.getInventory().getItemInMainHand().getEnchantments().isEmpty()) {
                        Utils.chat(player, "&eItem Enchantments: &c ====================================");
                        Map<Enchantment, Integer> ench = player.getInventory().getItemInMainHand().getEnchantments();
                        for (Map.Entry<Enchantment, Integer> e : ench.entrySet()) {
                            String str = e.getKey().getKey().getKey().replace("_", " ");
                            String name = str.substring(0, 1).toUpperCase() + str.substring(1);
                            int level = e.getValue();
                            String output = "      &b" + name + " : " + level;
                            Utils.chat(player, output);
                        }
                    }
                } else {
                    Utils.chat(Main.messages.getString("ItemIsAir"));
                }
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
            return true;
        }
        Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        return false;
    }
}
