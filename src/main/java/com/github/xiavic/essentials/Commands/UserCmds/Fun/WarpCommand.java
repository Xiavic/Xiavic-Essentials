package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.warp.Warp;
import com.github.xiavic.essentials.Utils.warp.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.github.xiavic.essentials.Main.messages;

public class WarpCommand implements TabExecutor {

    private static final WarpManager warpManager = WarpManager.INSTANCE;

    //SYNTAX: /warp <warpName/create/delete/whitelist/blacklist/info> <player | on/off/add/remove | warpName> <player>
    @Override public boolean onCommand(@NotNull final CommandSender sender,
        @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (args.length >= 1) {

            switch (args[0].toLowerCase()) {
                case "create":
                    if (sender.hasPermission(messages.getString("WarpCreation"))) {
                        if (sender instanceof Player) {
                            if (args.length < 2) {
                                Utils.chat(sender, messages.getString("InvalidArgs")
                                    .replace("%reason", "Please specify a warpName"));
                                return true;
                            }
                            final String targetName = args[1];
                            Optional<Warp> optionalWarp = warpManager.getWarp(targetName);
                            if (optionalWarp.isPresent()) {
                                Utils.chat(sender, messages.getString("WarpExists"));
                                return true;
                            }
                            Warp targetWarp = new Warp(targetName, ((Player) sender).getLocation());
                            warpManager.registerWarp(targetWarp);
                        }
                    } else {
                        Utils.chat(sender, messages.getString("NoPerms"));
                        return true;
                    }
                case "delete":
                case "remove":
                    if (sender.hasPermission(messages.getString("WarpDeletion"))) {
                        if (args.length < 2) {
                            Utils.chat(sender, messages.getString("InvalidArgs")
                                .replace("%reason", "Please specify a warpName"));
                            return true;
                        }
                        final String targetName = args[1];
                        Optional<Warp> optionalWarp = warpManager.getWarp(targetName);
                        if (!optionalWarp.isPresent()) {
                            Utils.chat(sender, messages.getString("InvalidArgs")
                                .replace("%reason", "Warp \"" + targetName + "\" not found!"));
                            return true;
                        }
                        final Warp warp = optionalWarp.get();
                        warpManager.unregisterWarp(warp);
                        Utils.chat(sender, messages.getString("warpUnsafe!"));
                        final Entity player = (Entity) sender;
                        if (warp.isEnabled()) {
                            if (warp.canBeAccessedBy(player)) {
                                if (!warp.teleport(player)) {
                                    Utils.chat(player, messages.getString("WarpUnsafe"));
                                    return true;
                                }
                            } else {
                                Utils.chat(player, messages.getString("WarpDenied"));
                                return true;
                            }

                        } else {
                            Utils.chat(sender, messages.getString("WarpDisabled"));
                        }
                    } else {
                        Utils.chat(sender, messages.getString("NoPerms"));
                    }
                    return true;
            }
        }
        return true;
    }

    @Nullable @Override public List<String> onTabComplete(@NotNull final CommandSender sender,
        @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        return null;
    }
}
