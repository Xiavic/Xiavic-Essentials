package com.github.xiavic.essentials.Commands.UserCmds.Essential;

import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static com.github.xiavic.essentials.Main.messages;
import static com.github.xiavic.essentials.Main.permissions;

/**
 * Represents a command to repair the item in the player's hand (main hand)
 */
public class RepairCommand implements CommandExecutor {

    private static final String permission = permissions.getString("Repair");

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
                             @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission(permission)) {
            Utils.chat(sender, messages.getString("NoPerms"));
            return true;
        }
        if (!(sender instanceof Player)) {
            Utils.chat(sender, messages.getString("SenderNotPlayer"));
            return true;
        }
        final String message;
        if (doRepair(((Player) sender).getInventory().getItemInMainHand())) {
            message = messages.getString("Repair_Success");
        } else {
            message = messages.getString("Repair_Failure");
        }
        Utils.chat(sender, message);
        return true;
    }

    private boolean doRepair(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (!(meta instanceof Damageable)) {
            return false;
        }
        Damageable damageable = (Damageable) meta;
        damageable.setDamage(0);
        itemStack.setItemMeta(meta);
        return true;
    }
}
