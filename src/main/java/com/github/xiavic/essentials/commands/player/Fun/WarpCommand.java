package com.github.xiavic.essentials.commands.player.Fun;

import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.warp.Warp;
import com.github.xiavic.essentials.Utils.warp.WarpManager;
import org.bukkit.Bukkit;
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

public class WarpCommand implements TabExecutor {

    private static final WarpManager warpManager = WarpManager.INSTANCE;

    //SYNTAX: /warp <warpName/info> <player | warpName> <play er>
    @Override
    public boolean onCommand(@NotNull final CommandSender sender,
                             @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length <= 1) {
            if (args.length == 1) {
                final String targetWarp = args[0];
                final Optional<Warp> optionalWarp = warpManager.getWarp(targetWarp);
                if (sender instanceof Player) {
                    if (optionalWarp.isPresent()) {
                        final Warp warp = optionalWarp.get();
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
            final Optional<Warp> optionalWarp = warpManager.getWarp(targetWarp);
            if (optionalWarp.isPresent()) {
                final Warp warp = optionalWarp.get();
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender,
                                      @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        if (args.length <= 1) {
            if (sender instanceof Player) {
                Stream<Warp> warps =
                        warpManager.getAccessibleToPermissible(sender).stream().filter(Warp::isBaseWarp)
                                .filter(Warp::isEnabled);
                Stream<String> strings = warps.map(Warp::getName).sorted(Comparator.naturalOrder());
                return args.length == 0 ?
                        strings.collect(Collectors.toList()) :
                        strings.filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            }
        } else {
            //Args length >= 2
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
