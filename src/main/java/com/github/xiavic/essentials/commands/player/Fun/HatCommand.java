package com.github.xiavic.essentials.commands.player.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.github.xiavic.essentials.Main.messages;

public class HatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Hat")) || player.isOp()) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                ItemStack head = player.getInventory().getHelmet();
                if (!(player.getInventory().firstEmpty() == -1)) {
                    player.getInventory().addItem(head);
                    player.getInventory().setHelmet(hand);
                    return true;
                } else
                    Utils.chat(player, messages.getString("FullInventory"));
            } else {
                Utils.chat(player, messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
