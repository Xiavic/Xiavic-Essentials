package com.github.xiavic.essentials.commands.player.Essential.Teleport;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.LocationUtils;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SpawnCommand implements CommandExecutor {

    private static ITeleportHandler teleportHandler;

    public static void loadTeleportHandler() {
        RegisteredServiceProvider<ITeleportHandler> rsp = Bukkit.getServicesManager().getRegistration(ITeleportHandler.class);
        if (rsp != null) {
            teleportHandler = rsp.getProvider();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        Location l = LocationUtils.getLocation("Spawn");
        if (args.length == 1) {
            if (player.hasPermission(Main.permissions.getString("SpawnOthers")) || player.isOp()) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    teleportHandler.teleport(target, l);
                    return true;
                } else {
                    Utils.chat(player, Main.messages.getString("PlayerNotFound"));
                }
            }
            Utils.chat(player, Main.messages.getString("NoPerms"));
        } else {
            if (player.hasPermission(Main.permissions.getString("Spawn")) || player.isOp()) {
                teleportHandler.teleport(player, l);
                return true;
            }
            Utils.chat(player, Main.messages.getString("NoPerms"));
        }
        return false;
    }
}
