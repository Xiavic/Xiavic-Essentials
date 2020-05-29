package com.github.xiavic.essentials.Commands.staff.noncheat.teleport;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TpallCommand implements CommandExecutor {
    private static ITeleportHandler teleportHandler;

    public static void loadHandler() {
        RegisteredServiceProvider<ITeleportHandler> rsp = Bukkit.getServicesManager().getRegistration(ITeleportHandler.class);
        if (rsp != null) {
            teleportHandler = rsp.getProvider();
        }
    }

    // TODO probably ought to create messages in the config files but it can stay like this for now

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission(Main.permissions.getString("Tpall")) || player.isOp()) {
                if (strings.length == 1) {
                    Player target1 = Bukkit.getPlayer(strings[0]);
                    if (target1 != null) {
                        if (teleportHandler.isDisabled(target1)) {
                            Utils.chat(player,
                                    "Teleporting all players to %target%, I'm sure they'll be happy about it"
                                            .replace("%target%", target1.getDisplayName()));
                            for (Player target : Bukkit.getOnlinePlayers()) {
                                if (target != target1) {
                                    boolean result =
                                            teleportHandler.teleport(target1, target, true);
                                    if (result)
                                        Utils.chat(target, "You are being teleported!");
                                }
                            }
                        }
                    }
                } else {
                    Utils.chat(player,
                            "Teleporting all players to you, I'm sure they'll be happy about it");
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        if (target != player) {
                            boolean result = teleportHandler.teleport(player, target, true);
                            if (result)
                                Utils.chat(target, "You are being teleported!");
                        }
                    }
                }
                return true;
            }
        }
        Utils.chat(commandSender, Main.messages.getString("SenderNotPlayer"));
        return false;
    }
}
