package com.github.xiavic.essentials.Commands.player.Fun;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the command used to obtain a head for a given player.
 */
public class HeadCommand implements TabExecutor {

    public HeadCommand() {
        Main.getInstance().getCommand("head").setAliases(Collections.singletonList("skull"));
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender commandSender,
                             @NotNull final Command command, @NotNull final String s, @NotNull final String[] strings) {
        if (strings.length < 1) {
            Utils.chat(commandSender, Main.messages.getString("SpecifyTarget"));
            return true;
        }
        if (!(commandSender instanceof Player)) {
            Utils.chat(commandSender, Main.messages.getString("SenderNotPlayer"));
            return true;
        }
        final Player sender = (Player) commandSender;
        final String playerName = strings[0];
        final OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if (!target.hasPlayedBefore()) {
            Utils.chat(commandSender, Main.messages.getString("PlayerNotFound"));
            return false;
        }
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(target);
        skull.setItemMeta(skullMeta);
        Utils.placeInCursorSlot(sender, skull); //TODO add a fail/success message.
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender commandSender,
                                                @NotNull final Command command, @NotNull final String s, @NotNull final String[] strings) {
        Stream<String> stream = Bukkit.getOnlinePlayers().stream().map(Player::getName);
        if (strings.length > 0) {
            stream = stream.filter(string -> string.startsWith(strings[0]));
        }
        return stream.sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}
