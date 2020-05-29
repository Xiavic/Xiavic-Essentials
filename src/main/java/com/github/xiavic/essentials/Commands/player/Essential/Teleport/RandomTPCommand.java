package com.github.xiavic.essentials.Commands.player.Essential.Teleport;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;


public class RandomTPCommand implements CommandExecutor {

    private static ITeleportHandler teleportHandler;
    private final Main plugin = Main.getInstance();

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
            if (player.hasPermission(Main.permissions.getString("RandomTP")) || player.isOp()) {
                double randomX = getCoord();
                double randomZ = getCoord();
                double randomY = player.getWorld().getHighestBlockYAt((int) randomX, (int) randomZ) + 1.5;
                Location rtp = new Location(player.getWorld(), randomX, randomY, randomZ);
//                player.teleport(rtp);
                teleportHandler.teleport(player, rtp);
                Utils.chat(player, Main.messages.getString("RTP"));
                Block block = rtp.getBlock().getRelative(0, -1, 0);
                if (block.getType().equals(Material.WATER) || block.getType().equals(Material.LAVA)) {
                    block.setType(Material.DIRT);
                }
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }

    public double getCoord() {
        double distance = plugin.getConfig().getDouble("RTPDistance");
        return (Math.random() * (distance * 2)) - distance;
    }

}
