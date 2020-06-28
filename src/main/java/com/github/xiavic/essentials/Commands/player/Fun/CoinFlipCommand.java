package com.github.xiavic.essentials.Commands.player.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CoinFlipCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Main.permissions.getString("Coinflip")) || player.isOp()) {
                Random r = new Random();
                int i = r.nextInt((2 - 1) + 1) + 1;
                if (i == 1) {
                    Utils.chat(player, Main.messages.getString("Heads"));
                } else {
                    Utils.chat(player, Main.messages.getString("Tails"));
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
