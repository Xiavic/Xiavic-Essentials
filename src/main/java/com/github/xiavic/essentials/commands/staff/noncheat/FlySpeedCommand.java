package com.github.xiavic.essentials.commands.staff.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlySpeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission(Main.permissions.getString("FlySpeed")) || player.isOp()) {
                    final String playerspeed = args[0];
                    final int speed;
                    try {
                        speed = Integer.parseInt(playerspeed);
                    } catch (NumberFormatException ex) {
                        Utils.chat(player, "Invalid Number");
                        return true;
                    }
                    if (speed > 0 && speed <= 10) {
                        player.setFlySpeed(speed / 10f);
                    } else {
                        Utils.chat(player, "Invalid Number");
                    }
                    Utils.chat(player, Main.messages.getString("FlySpeed").replace("%amount%", playerspeed));
                    return true;
                } else {
                    Utils.chat(player, Main.messages.getString("NoPerms"));
                }
            }
            return true;
        }
        Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        return false;
    }
}
