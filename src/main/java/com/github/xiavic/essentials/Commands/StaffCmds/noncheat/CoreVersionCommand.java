package com.github.xiavic.essentials.Commands.StaffCmds.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO: Refactor Utils

public class CoreVersionCommand implements CommandExecutor {
    Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Version") || player.isOp()) {
                String version = plugin.getDescription().getVersion();
                player.sendMessage(Utils.chat(Main.messages.getString("Version").replace("%version%", version)));
                return true;
            } else {
                player.sendMessage(Utils.chat(Main.messages.getString("NoPerms")));
            }
        } else {
            String version = plugin.getDescription().getVersion();
            sender.sendMessage(Utils.chat(Main.messages.getString("Version").replace("%version%", version)));
            return true;
        }
        return false;
    }

}
