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

                Main.mainConfig.forceReload();
                Main.messages.forceReload();
                Main.permissions.forceReload();

                Utils.chat(player, Main.messages.getString("ConfigUpdate"));
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            Main.mainConfig.forceReload();
            Main.messages.forceReload();
            Main.permissions.forceReload();
            Utils.chat(sender, Main.messages.getString("ConfigUpdate"));
            return true;
        }
        return false;
    }
}
