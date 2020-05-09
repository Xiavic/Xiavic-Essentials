package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class PonyCommand implements CommandExecutor {

    private final static ChatColor[] rainbow = {
            ChatColor.RED,
            ChatColor.GOLD,
            ChatColor.YELLOW,
            ChatColor.GREEN,
            ChatColor.BLUE,
            ChatColor.DARK_BLUE,
            ChatColor.DARK_PURPLE,};
    private static final String[] ponies = {
            Utils.chat("&fRarity is best pony!"),
            Utils.chat("&6AppleJack is best pony!"),
            Utils.chat("&eFluttershy is best pony"),
            Utils.chat("&eFlutterBitch is best bitch."),
            Utils.chat("&eFlutterHurricane!!!!!!!!1"),
            Utils.chat("&5Twilight sparkle is best pony!"),
            Utils.chat("&1Luna is best pony!"),
            Utils.chat("&dand then I said, OATMEAL?! ARE YOU CRAZY -Pinkie Pie"),
            rainbowizeString("RainbowDash") + Utils.chat(" &9is best pony"),
            Utils.chat("&eDerpyHooves is derpy..."),
            rainbowizeString("SONIC RAINBOOM"),
            rainbowizeString("ATOMIC RAINBOOM"),
            rainbowizeString("RAINBOW FIRE TRAIL!"),
            rainbowizeString("MLP:FIM IS TEH BEST!"),
            rainbowizeString("Pony Swag")};
    private static final Random r = new Random();

    public static String rainbowizeString(String string) {
        StringBuilder sb = new StringBuilder();
        int rb = 0;
        char[] chars = string.toCharArray();
        for (char aChar : chars) {
            if ((int) aChar != 32) {
                sb.append(rainbow[rb]);
            }
            sb.append(aChar);
            if (++rb >= rainbow.length) {
                rb = 0;
            }
        }
        return sb.toString();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(Main.permissions.getString("Pony"))) {
            player.sendMessage(Utils.chat(Main.messages.getString("Pony")).replace("%Random_Pony%", ponies[r.nextInt(ponies.length)]).replace("%PonyPerson%", Utils.chat(rainbowizeString(Main.messages.getString("PonyPerson")))));
        } else {
            player.sendMessage(Utils.chat(Main.messages.getString("NoPerms")));
        }
        return true;
    }
}
