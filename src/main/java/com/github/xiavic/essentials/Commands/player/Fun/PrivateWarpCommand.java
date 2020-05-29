package com.github.xiavic.essentials.Commands.player.Fun;

import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.warp.PrivateWarp;
import com.github.xiavic.essentials.Utils.warp.PrivateWarpManager;
import com.github.xiavic.essentials.Utils.warp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.xiavic.essentials.Main.messages;
import static com.github.xiavic.essentials.Main.permissions;

public class PrivateWarpCommand implements TabExecutor {

    private static final PrivateWarpManager warpManager = PrivateWarpManager.INSTANCE;


    //SYNTAX: /privatewarp <warpName/info> <playerName | warpName> <player>
    //SYNTAX /privatewarp andrewandy:test OR /pw info andrewandy:test
    @Override
    public boolean onCommand(@NotNull final CommandSender sender,
                             @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length <= 1) {
            if (args.length == 1) {
                final String targetWarp = args[0];
                final String[] split = targetWarp.split(":");
                if (split.length < 2) {
                    return false;
                }
                final String owner = split[0], targetName = split[1];
                final OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
                //Filter private warp
                final Optional<PrivateWarp> optionalWarp =
                        warpManager.getWarp(player.getUniqueId(), targetName);
                if (sender instanceof Player) {
                    if (optionalWarp.isPresent()) {
                        final PrivateWarp warp = optionalWarp.get();
                        if (warp.canBeAccessedBy(sender)) {
                            if (warp.isEnabled()) {
                                if (!warp.teleport((Player) sender)) {
                                    Utils.chat(sender, messages.getString("WarpUnsafe"));
                                }
                            } else {
                                Utils.chat(sender, messages.getString("WarpDisabled"));
                            }
                        } else {
                            Utils.chat(sender, messages.getString("WarpDenied"));
                        }
                    } else {
                        Utils.chat(sender,
                                messages.getString("InvalidArgs").replace("%reason%", "No such warp!"));
                    }
                } else {
                    Utils.chat(sender, messages.getString("SenderNotPlayer"));
                }
            } else {
                Utils.chat(sender, messages.getString("SpecifyTarget"));
                return true;
            }
        } else {
            final String targetWarp = args[0];
            final String[] split = targetWarp.split(":");
            if (split.length < 2) {
                return false;
            }
            final String owner = split[0], targetName = split[1];
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(owner);
            //Filter private warp
            final Optional<PrivateWarp> optionalWarp =
                    warpManager.getWarp(offlinePlayer.getUniqueId(), targetName);
            if (optionalWarp.isPresent()) {
                final PrivateWarp warp = optionalWarp.get();
                if (sender.hasPermission(permissions.getString("WarpOthers"))) {
                    if (warp.isEnabled()) {
                        for (int index = 1; index < args.length; ) {
                            final String targetPlayer = args[index++];
                            final Player player = Bukkit.getPlayer(targetPlayer);
                            if (player != null) {
                                if (warp.canBeAccessedBy(player)) {
                                    if (!warp.teleport(player)) {
                                        Utils.chat(sender, messages.getString("WarpOtherFailed")
                                                .replace("%reason%", messages.getString("WarpUnsafe")));
                                    }
                                } else {
                                    Utils.chat(sender, messages.getString("WarpOtherFailed")
                                            .replace("%reason%", messages.getString("WarpDenied")));
                                }
                            }
                            Utils.chat(sender, messages.getString("PlayerNotFound"));
                        }
                    } else {
                        Utils.chat(sender, messages.getString("WarpDisabled"));
                    }
                } else {
                    Utils.chat(sender, messages.getString("NoPerms"));
                    return true;
                }
                return true;
            } else {
                Utils.chat(sender,
                        messages.getString("InvalidArgs").replace("%reason%", "No such warp!"));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
                                                @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        if (args.length <= 1) {
            if (sender instanceof Player) {
                String[] split = args[0].split(":");
                if (split.length < 1) {
                    Stream<String> playerStream =
                            Bukkit.getOnlinePlayers().stream().map(Player::getName)
                                    .filter(s -> s.startsWith(args[0]));
                    return playerStream.sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());
                }
                final String targetOwner = split[0];
                final String targetName = split[1];
                final OfflinePlayer player = Bukkit.getOfflinePlayer(targetOwner);
                return warpManager.getWarps(
                        privateWarp -> privateWarp.getOwner().equals(player.getUniqueId())
                                && privateWarp.getName().equalsIgnoreCase(targetName)).map(Warp::getName)
                        .collect(Collectors.toList());
            }
        } else {
            //Args length > 2
            List<String> strings = new ArrayList<>(Arrays.asList(args));
            strings.remove(0);
            Stream<String> stringStream = Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(s -> !strings.contains(s)).sorted(Comparator.naturalOrder());
            if (args.length > 2) {
                return stringStream.filter(s -> s.startsWith(args[args.length - 1]))
                        .collect(Collectors.toList());
            } else {
                return stringStream.collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
