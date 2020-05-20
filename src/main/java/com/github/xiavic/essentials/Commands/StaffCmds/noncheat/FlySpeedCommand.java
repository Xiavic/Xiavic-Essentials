package com.github.xiavic.essentials.Commands.StaffCmds.noncheat;

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
                        // TODO: Refactor For Less If Statements - Add Checks for  speed < 1 & speed > 10
                        String speed = args[0];
                        if (speed.equalsIgnoreCase("1")) {
                            player.setFlySpeed((float) 0.1);
                        } else if (speed.equalsIgnoreCase("2")) {
                            player.setFlySpeed((float) 0.2);
                        } else if (speed.equalsIgnoreCase("3")) {
                            player.setFlySpeed((float) 0.3);
                        } else if (speed.equalsIgnoreCase("4")) {
                            player.setFlySpeed((float) 0.4);
                        } else if (speed.equalsIgnoreCase("5")) {
                            player.setFlySpeed((float) 0.5);
                        } else if (speed.equalsIgnoreCase("6")) {
                            player.setFlySpeed((float) 0.6);
                        } else if (speed.equalsIgnoreCase("7")) {
                            player.setFlySpeed((float) 0.7);
                        } else if (speed.equalsIgnoreCase("8")) {
                            player.setFlySpeed((float) 0.8);
                        } else if (speed.equalsIgnoreCase("9")) {
                            player.setFlySpeed((float) 0.9);
                        } else if (speed.equalsIgnoreCase("10")) {
                            player.setFlySpeed(1);
                        } else {
                            Utils.chat(player, Main.messages.getString("InvalidNumber"));
                        }
                        Utils.chat(player, Main.messages.getString("FlySpeed").replace("%amount%", speed));
                    } else {
                        Utils.chat(player, Main.messages.getString("NoPerms"));
                    }
                    return true;
                }
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
