package com.github.xiavic.essentials.Commands.staff.cheats;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheatArmor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("CheatArmor")) || player.isOp()) {
                if (command.getName().equalsIgnoreCase("cheatarmor")) {
                    ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
                    helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4); // Default - 4
                    helmet.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4); // Default - 4
                    helmet.addEnchantment(Enchantment.PROTECTION_FIRE, 4); // Default - 4
                    helmet.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4); // Default - 4
                    helmet.addEnchantment(Enchantment.WATER_WORKER, 1);  // Default - 1
                    helmet.addEnchantment(Enchantment.OXYGEN, 3);  // Default - 3
                    helmet.addEnchantment(Enchantment.DURABILITY, 3);  // Default - 3
                    ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4); // Default - 4
                    chestplate.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4); // Default - 4
                    chestplate.addEnchantment(Enchantment.PROTECTION_FIRE, 4); // Default - 4
                    chestplate.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4); // Default - 4
                    chestplate.addEnchantment(Enchantment.THORNS, 3);  // Default - 3
                    chestplate.addEnchantment(Enchantment.DURABILITY, 3);  // Default - 3
                    ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS);
                    legs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4); // Default - 4
                    legs.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4); // Default - 4
                    legs.addEnchantment(Enchantment.PROTECTION_FIRE, 4); // Default - 4
                    legs.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4); // Default - 4
                    legs.addEnchantment(Enchantment.THORNS, 3);  // Default - 3
                    legs.addEnchantment(Enchantment.DURABILITY, 3);  // Default - 3
                    ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                    boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4); // Default - 4
                    boots.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4); // Default - 4
                    boots.addEnchantment(Enchantment.PROTECTION_FIRE, 4); // Default - 4
                    boots.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4); // Default - 4
                    boots.addEnchantment(Enchantment.DURABILITY, 3);  // Default - 3
                    boots.addEnchantment(Enchantment.PROTECTION_FALL, 4); // Default - 4

                    player.getInventory().addItem(helmet);
                    player.getInventory().addItem(chestplate);
                    player.getInventory().addItem(legs);
                    player.getInventory().addItem(boots);
                    Utils.chat(player, Main.messages.getString("CheatArmor"));

                }
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
            return true;
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
