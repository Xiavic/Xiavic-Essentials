package com.github.xiavic.essentials.commands.player.Essential;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(Main.permissions.getString("Suicide")) || player.isOp()) {
            player.setHealth(0);
            return true;
        } else {
            Utils.chat(player, Main.messages.getString("NoPerms"));
        }
        return false;
    }
}
