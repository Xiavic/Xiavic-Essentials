package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.TeleportHandler;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TopCommand implements CommandExecutor {
    private static ITeleportHandler teleportHandler;

    public static void loadTeleportHandler() {
        RegisteredServiceProvider<ITeleportHandler> rsp = Bukkit.getServicesManager().getRegistration(ITeleportHandler.class);
        if (rsp != null) {
            teleportHandler = rsp.getProvider();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Top"))) {
                teleportHandler.teleport(player, player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().add(0, 1.5, 0));
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
//Found an easier way to do the command, Thanks to @Lokka30! <3
