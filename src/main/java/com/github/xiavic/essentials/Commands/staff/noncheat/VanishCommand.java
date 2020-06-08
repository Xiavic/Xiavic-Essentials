package com.github.xiavic.essentials.Commands.staff.noncheat;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor {

    List<Player> vanished = new ArrayList<>();

    //Code is deprecated, but should still work overall.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Vanish")) || player.isOp()) {
                if (vanished.contains(player)) {
                    vanished.remove(player);
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        target.showPlayer(player);
                    }
                    Utils.chat(player, Main.messages.getString("VanishedPlayer").replace("%mode%", Main.messages.getString("VanishOff")));
                } else {
                    vanished.add(player);
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        target.hidePlayer(player);
                    }
                    Utils.chat(player, Main.messages.getString("VanishedPlayer").replace("%mode%", Main.messages.getString("VanishOn")));
                }
            } else {
                Utils.chat(player, Main.messages.getString("NoPerms"));
            }
        } else {
            Utils.chat(sender, Main.messages.getString("SenderNotPlayer"));
        }
        return false;
    }
}
