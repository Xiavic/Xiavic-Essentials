package com.github.xiavic.essentials.Commands.UserCmds.Fun;
import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import static com.github.xiavic.essentials.Main.*;

public class HatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Hat")) || player.isOp()) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (player.getInventory().getHelmet().getAmount() == 0) {
                    player.getInventory().setHelmet(hand);
                    return true;
                } else
                    Utils.chat(player, messages.getString("EmptyHeadSlot"));
            } else {
                Utils.chat(player, messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
