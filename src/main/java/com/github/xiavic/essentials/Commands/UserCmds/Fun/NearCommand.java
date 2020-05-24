package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

// TODO: Refactor Required

public class NearCommand implements CommandExecutor {

    private double radius = Main.mainConfig.getDouble("NearRadius");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Near")) || player.isOp()) {
                boolean found = false;
                ArrayList<String> nearbyPlayers = new ArrayList<>();
                for (Player target : Bukkit.getOnlinePlayers()) {
                    double distance = player.getLocation().distance(target.getLocation());
                    if (distance <= radius) {
                        found = true;
                        nearbyPlayers.add("    " + target.getName() + ": " + distance + "m");
                    }
                }
                if (found) {
                    Utils.chat(player, "List of nearby players:");
                    for (String s : nearbyPlayers) {
                        Utils.chat(player, s);
                    }
                } else {
                    Utils.chat(player, "No players are nearby!");
                }
                return true;
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
            return true;
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
