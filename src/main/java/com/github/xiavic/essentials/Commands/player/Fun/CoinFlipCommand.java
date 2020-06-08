//package com.github.xiavic.essentials.Commands.player.Fun;
//
//import com.github.xiavic.essentials.Main;
//import com.github.xiavic.essentials.Utils.Utils;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//import java.util.Random;
//
//public class CoinFlipCommand implements CommandExecutor {
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//
//        private static int getRandomRange ( int min, int max){
//            Random random = new Random();
//            return random.ints(min, (max + 1)).findFirst().getAsInt();
//
//        }
//        if (sender instanceof Player) {
//            Player player = (Player) sender;
//            if (player.hasPermission(Main.permissions.getString("CoinFlip")) || player.isOp()) {
//
//                Utils.chat(Main.messages.getString("Coinflip").replace("%flip%", getRandomRange(0, 1)));
//            }
//        }
//        return false;
//    }
//}
