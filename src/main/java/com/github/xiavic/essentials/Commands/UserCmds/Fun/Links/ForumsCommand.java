package com.github.xiavic.essentials.Commands.UserCmds.Fun.Links;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForumsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if (player.hasPermission(Main.permissions.getString("Forums")) || player.isOp()) {
            player.sendMessage(Utils.chat(Main.messages.getString("Forums")));
        } else {
            player.sendMessage(Utils.chat(Main.messages.getString("NoPerms")));
        }
        return false;
    }
}
