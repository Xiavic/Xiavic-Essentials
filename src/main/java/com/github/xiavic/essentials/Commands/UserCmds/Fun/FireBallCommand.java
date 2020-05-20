package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;


public class FireBallCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Fireball")) || player.isOp()) {
                //double zoom = 2;
                //final Vector direction = player.getEyeLocation().getDirection().multiply(zoom);
                //Projectile fireball = player.getWorld().spawn(player.getEyeLocation(direction.getX(), direction.getY(), direction.getZ()) Fireball.class);

            }
        }
        return false;
    }
}
