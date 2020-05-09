package com.github.xiavic.essentials.Commands.StaffCmds.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO: Refactor Utils
public class ConfigReloadCommand implements CommandExecutor {
    Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("ConfigUpdate")) || player.isOp()) {
                // plugin.updateShit(); // TODO: Replace and Make Lightning File

                Main.mainConfig.forceReload();
                Main.messages.forceReload();
                Main.permissions.forceReload();

                player.sendMessage(Utils.chat(Main.messages.getString("ConfigUpdate")));
                return true;
            } else {
                player.sendMessage(Utils.chat(Main.messages.getString("NoPerms")));
            }
        } else {
            // plugin.updateShit();
            Main.mainConfig.forceReload();
            Main.messages.forceReload();
            Main.permissions.forceReload();
            sender.sendMessage(Utils.chat(Main.messages.getString("ConfigUpdate")));
            return true;
        }
        return false;

    }
}
