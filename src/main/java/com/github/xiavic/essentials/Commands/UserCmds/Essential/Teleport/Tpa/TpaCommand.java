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

public class TpaCommand implements CommandExecutor {

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
            if (player.hasPermission(Main.permissions.getString("Tpa")) || player.isOp()) {
                if (strings.length == 1) {
                    if (!strings[0].equalsIgnoreCase(player.getName())) {
                        if (tpaHandler.canTpa(player)) {
                            try {
                                Player target = Bukkit.getPlayer(strings[0]);
                                int result = tpaHandler.addRequest(player, target);
                                if (result == 2) {
                                    Utils.chat(player, Main.messages.getString("TpDisabled").replace("%target%", target.getDisplayName()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                            }
                        }
                    } else {
                        Utils.chat(player, Main.messages.getString("TpSelf"));
                    }
                    return true;
                } else {
                    Utils.chat(player, "You must specify a target!");
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
