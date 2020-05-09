package com.github.xiavic.essentials.Commands.UserCmds.Essential.Teleport.Tpa;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Tpa.TpaHandler;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportRequestHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TpdenyCommand implements CommandExecutor {
    private static ITeleportRequestHandler tpaHandler;

    public static void loadTPAHandler() {
        RegisteredServiceProvider<ITeleportRequestHandler> rsp = Bukkit.getServicesManager().getRegistration(ITeleportRequestHandler.class);
        if (rsp != null) {
            tpaHandler = rsp.getProvider();
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission(Main.permissions.getString("TpDeny")) || player.isOp()) {
                tpaHandler.parseRequest(player, false);
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
            return true;
        } else {
            Utils.chat(commandSender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
