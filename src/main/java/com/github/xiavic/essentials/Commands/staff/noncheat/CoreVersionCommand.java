package com.github.xiavic.essentials.Commands.staff.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreVersionCommand implements CommandExecutor {
    Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Version") || player.isOp()) {
                String version = plugin.getDescription().getVersion();
                Utils.chat(player, Main.messages.getString("Version").replace("%version%", version));
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            String version = plugin.getDescription().getVersion();
            Utils.chat(sender, Main.messages.getString("Version").replace("%version%", version));
            return true;
        }
        return false;
    }

}
