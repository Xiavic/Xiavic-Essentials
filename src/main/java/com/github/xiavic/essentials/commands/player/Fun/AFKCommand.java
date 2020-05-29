package com.github.xiavic.essentials.commands.player.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.AFKHandler;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AFKCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
                             @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission(Main.permissions.getString("Afk"))) {
            Utils.chat(sender, Main.messages.getString("NoPerms"));
            return true;
        }
        if (!(sender instanceof Player)) {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
            return true;
        }
        final UUID player = ((Player) sender).getUniqueId();
        final boolean currentlyAfk = AFKHandler.INSTANCE.isAFK(player);
        AFKHandler.INSTANCE.setAFK(player, !currentlyAfk);
        return true;
    }
}
