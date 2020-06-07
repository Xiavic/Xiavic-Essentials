package com.github.xiavic.essentials.Commands.player.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.AFKHandler;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AFKCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(Main.permissions.getString("Afk")) || p.isOp()) {
                final UUID player = ((Player) sender).getUniqueId();
                final boolean currentlyAfk = AFKHandler.INSTANCE.isAFK(player);
                AFKHandler.INSTANCE.setAFK(player, !currentlyAfk);
                return true;
            } else {
                Utils.chat(p, Main.messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
