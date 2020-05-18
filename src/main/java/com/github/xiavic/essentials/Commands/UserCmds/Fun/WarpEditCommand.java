package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.warp.Warp;
import com.github.xiavic.essentials.Utils.warp.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.xiavic.essentials.Main.messages;
import static com.github.xiavic.essentials.Main.permissions;

public class WarpEditCommand implements TabExecutor {

    private static final WarpManager warpManager = WarpManager.INSTANCE;

    //SYNTAX /warpedit <create/delete/toggle/permission> <name> <null | null | set/reset> <permission>
    @Override public boolean onCommand(@NotNull final CommandSender sender,
        @NotNull final Command command, @NotNull final String s, @NotNull final String[] args) {
        if (args.length >= 1) {

            switch (args[0].toLowerCase()) {
                case "add":
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
                            Warp targetWarp = new Warp(targetName, ((Player) sender).getLocation())
                                .setEnabled(true);
                            warpManager.registerWarp(targetWarp);
                            Utils.chat(sender,
                                messages.getString("WarpCreated").replace("%name%", targetName));
                        }
                    } else {
                        Utils.chat(sender, messages.getString("NoPerms"));
                    }
                    return true;
                case "delete":
                case "remove":
                    if (sender.hasPermission(permissions.getString("WarpDeletion"))) {
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
                        Utils.chat(sender, messages.getString("WarpDeleted"));
                        return true;
                    } else {
                        Utils.chat(sender, messages.getString("NoPerms"));
                    }
                    return true;
                case "toggle":
                    if (sender.hasPermission(permissions.getString("WarpToggle"))) {
                        if (args.length >= 4) {
                            final String targetWarp = args[1];
                            final String toggle = args[2];
                            final Optional<Warp> optionalWarp = warpManager.getWarp(targetWarp);
                            if (!optionalWarp.isPresent()) {
                                Utils.chat(sender, messages.getString("InvalidArgs")
                                    .replace("%reason%", "Warp not found."));
                                return true;
                            }
                            final boolean enabled;
                            if (toggle.equalsIgnoreCase("on") || toggle
                                .equalsIgnoreCase("enabled")) {
                                enabled = true;
                            } else if (toggle.equalsIgnoreCase("off") || toggle
                                .equalsIgnoreCase("disabled")) {
                                enabled = false;
                            } else {
                                Utils.chat(sender, messages.getString("InvalidArgs")
                                    .replace("%reason%",
                                        "Unknown toggle! Accepted Values: on/enabled, off/disabled."));
                                return true;
                            }
                            final Warp warp = optionalWarp.get();
                            warp.setEnabled(enabled);
                            Utils.chat(sender, messages.getString("WarpToggled")
                                .replace("%enabled%", enabled ? "enabled." : "disabled."));
                            return true;

                        } else {
                            Utils.chat(sender, messages.getString("SpecifyTarget"));
                        }
                    } else {
                        Utils.chat(sender, messages.getString("NoPerms"));
                    }
                case "permission":
                    if (sender.hasPermission(permissions.getString("WarpPermissionEdit"))) {
                        if (args.length >= 3) {
                            final String targetWarp = args[1];
                            final Optional<Warp> optionalWarp = warpManager.getWarp(targetWarp);
                            if (!optionalWarp.isPresent()) {
                                Utils.chat(sender, messages.getString("InvalidArgs")
                                    .replace("%reason%", "No such warp!"));
                                return true;
                            }
                            final Warp warp = optionalWarp.get();
                            switch (args[2].toLowerCase()) {
                                case "set":
                                    if (args.length < 4) {
                                        Utils.chat(sender, messages.getString("SpecifyTarget"));
                                        return true;
                                    }
                                    final String permission =
                                        args[3].equalsIgnoreCase("null") ? null : args[3];
                                    warp.setPermission(permission);
                                    Utils.chat(sender, messages.getString("WarpPermissionEdited"));
                                    return true;
                                case "reset":
                                    warp.setPermission(null);
                                    Utils.chat(sender, messages.getString("WarpPermissionEdited"));
                                    return true;
                                default:
                                    Utils.chat(sender, messages.getString("InvalidArgs"
                                        .replace("%reason%", "Accepted Values: set, reset")));
                            }
                            final String message = warp.hasPermission() ?
                                messages.getString("WarpPermission")
                                    .replace("%permission%", warp.getPermission()) :
                                messages.getString("WarpNullPermission");
                            Utils.chat(sender, message);
                        } else {
                            Utils.chat(sender, messages.getString("SpecifyTarget"));
                        }
                        return true;
                    }
            }
        } else {
            Utils.chat(sender, messages.getString("NoPerms"));
        }
        return true;
    }

    @Override public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
        @NotNull final Command command, @NotNull final String s, @NotNull final String[] args) {

        List<String> commands = new ArrayList<>(4);
        if (sender.hasPermission(permissions.getString("WarpToggle"))) {
            commands.add("toggle");
        }
        if (sender.hasPermission(permissions.getString("WarpPermissionEdit"))) {
            commands.add("permission");
        }
        if (sender.hasPermission(permissions.getString("WarpDeletion"))) {
            commands.add("delete");
        }
        if (sender.hasPermission(permissions.getString("WarpCreation"))) {
            commands.add("create");
        }
        switch (args.length) {
            case 1:
                new ArrayList<>(commands)
                    .removeIf(str -> !str.startsWith(args[0]) && !str.equalsIgnoreCase(args[0]));
                return commands;
            case 0:
                return commands;
            case 2:
                return warpManager.getAccessibleToPermissible(sender).stream().map(Warp::getName)
                    .filter(str -> str.startsWith(args[0]) || str.equalsIgnoreCase(args[0]))
                    .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
            case 3:
                if (args[1].equalsIgnoreCase("permission") && commands.contains("permission")) {
                    return Arrays.stream(new String[] {"set", "reset"})
                        .filter(str -> str.startsWith(args[2]) || str.equalsIgnoreCase(args[2]))
                        .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                } else if (args[1].equalsIgnoreCase("toggle") && commands.contains("toggle")) {
                    return Arrays.stream(new String[] {"on", "off", "enabled", "disabled"})
                        .filter(str -> str.startsWith(args[2]) || str.equalsIgnoreCase(args[2]))
                        .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                }
        }
        return Collections.emptyList();
    }
}
