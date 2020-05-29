package com.github.xiavic.essentials.Commands.player.Essential.Teleport;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class BackCommand implements CommandExecutor {

    private static ITeleportHandler teleportHandler;

    public static void loadTeleportHandler() {
        RegisteredServiceProvider<ITeleportHandler> rsp = Bukkit.getServicesManager().getRegistration(ITeleportHandler.class);
        if (rsp != null) {
            teleportHandler = rsp.getProvider();
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission(Main.permissions.getString("Back")) || player.isOp()) {
                try {
                    teleportHandler.teleport(player, teleportHandler.getLastLocation(player));
                } catch (Exception e) {
                    Utils.chat(player, Main.messages.getString("BackNone"));
                }
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
            return true;
        }
        Utils.chat(commandSender, Main.messages.getString("SenderNotPlayer"));
        return false;
    }
}
