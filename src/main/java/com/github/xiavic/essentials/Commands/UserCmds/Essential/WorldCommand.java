package com.github.xiavic.essentials.Commands.UserCmds.Essential;

import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.xiavic.essentials.Main.messages;
import static com.github.xiavic.essentials.Main.permissions;

/**
 * Represents the command for /world - to change worlds.
 */
public class WorldCommand implements TabExecutor {

    private static final String permission = permissions.getString("World");
    private static final String noPermission = permissions.getString("NoPerms");
    private static final String noSuchWorld = messages.getString("NoSuchWorld");
    private static final String mustBePlayer = messages.getString("SenderNotPlayer");
    private static final String tpFailure = messages.getString("Tp_Failed");

    @Override
    public boolean onCommand(@NotNull final CommandSender commandSender,
                             @NotNull final Command command, @NotNull final String s, @NotNull final String[] strings) {
        if (!commandSender.hasPermission(permission) || !commandSender.isOp()) {
            Utils.chat(commandSender, noPermission);
            return true;
        }
        if (!(commandSender instanceof Player)) {
            Utils.chat(commandSender, mustBePlayer);
            return false;
        }
        if (strings.length < 1) {
            Utils.chat(commandSender, messages.getString("SpecifyTarget"));
            return true;
        }
        final World world = Bukkit.getWorld(strings[0]);
        if (world == null) {
            Utils.chat(commandSender, noSuchWorld);
            return true;
        }
        final Location location = world.getSpawnLocation();
        if (!((Player) commandSender)
                .teleport(location)) { //Teleport the player to the given world's spawn location.
            Utils.chat(commandSender, tpFailure);
        }
        return true;
    }

    /**
     * Tab completes based off of {@link Bukkit#getWorlds)}.
     */
    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull final CommandSender commandSender, @NotNull final Command command,
            @NotNull final String s, @NotNull final String[] strings) {
        if (!commandSender.hasPermission(permission) || !commandSender.isOp()) {
            return Collections.emptyList();
        }
        Stream<String> worlds = Bukkit.getWorlds().stream().map(World::getName);
        if (strings.length > 0) {
            worlds = worlds.filter(worldName -> worldName.startsWith(strings[0]));
        }
        return worlds.sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}
