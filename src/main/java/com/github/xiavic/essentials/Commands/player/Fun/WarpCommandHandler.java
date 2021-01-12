package com.github.xiavic.essentials.Commands.player.Fun;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.Messages;
import com.github.xiavic.essentials.Utils.messages.WarpMessages;
import com.github.xiavic.essentials.Utils.warp.PrivateWarp;
import com.github.xiavic.essentials.Utils.warp.PrivateWarpManager;
import com.github.xiavic.essentials.Utils.warp.Warp;
import com.github.xiavic.essentials.Utils.warp.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class WarpCommandHandler extends BaseCommand {

    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;
    private static final Messages messages = Messages.INSTANCE;
    private static final WarpMessages warpMessages = WarpMessages.INSTANCE;

    private final PrivateWarpManager privateWarpManager;
    private final WarpManager<Warp> warpManager;

    public WarpCommandHandler(@NotNull final BukkitCommandManager commandManager,
                              @NotNull final PrivateWarpManager pwManager,
                              @NotNull final WarpManager<Warp> warpManager) {
        commandManager.registerCommand(this);
        this.warpManager = warpManager;
        this.privateWarpManager = pwManager;
        commandManager.getCommandCompletions().registerAsyncCompletion("private_warps", context -> {
            final String owned = context.getConfig("owned", "true");
            if (Boolean.parseBoolean(owned)) {
                final String rawIndex = context.getConfig("index", "0");
                final int index = Integer.parseInt(rawIndex);
                final OfflinePlayer player = context.getContextValue(OfflinePlayer.class, index);
                return this.privateWarpManager
                        .getWarps((privateWarp -> privateWarp.getOwner().equals(player.getUniqueId())))
                        .map(Warp::getName).collect(Collectors.toList());
            }
            final String accessibleOnly = context.getConfig("accessible_only", "true");
            if (Boolean.parseBoolean(accessibleOnly)) {
                if (context.getPlayer() == null) {
                    return Collections.emptyList();
                }
                return this.privateWarpManager.getAccessibleToPermissible(context.getPlayer())
                        .stream().map(Warp::getName)
                        .collect(Collectors.toList()); //Async permission checkups are fine.
            }
            return Collections.emptyList();
        });
        commandManager.getCommandCompletions().registerAsyncCompletion("warps", context -> {
            final String accessibleOnly = context.getConfig("accessible_only", "true");
            if (Boolean.parseBoolean(accessibleOnly)) {
                return this.warpManager.getAccessibleToPermissible(context.getPlayer()).stream()
                        .map(Warp::getName)
                        .collect(Collectors.toList()); //Async permission checkups are fine.
            } else {
                return this.warpManager.getWarps().stream().map(Warp::getName)
                        .collect(Collectors.toList());
            }
        });
        commandManager.getCommandContexts().registerIssuerAwareContext(Warp.class, context -> {
            final String warpName = context.getResolvedArg("warpName", String.class);
            if (warpName == null) {
                throw new InvalidCommandArgument(); // TODO send message.
            }
            return this.warpManager.getWarp(warpName)
                    .map(warp -> warp.canBeAccessedBy(context.getSender()) ? warp : null)
                    .orElseThrow(InvalidCommandArgument::new); // TODO send message.
        });
        commandManager.getCommandContexts()
                .registerIssuerAwareContext(PrivateWarp.class, context -> {
                    final String warpName = context.getResolvedArg("warpName", String.class);
                    if (warpName == null) {
                        throw new InvalidCommandArgument(); // TODO send message.
                    }
                    String owner = context.getResolvedArg("owner", String.class);
                    if (owner == null && context.getPlayer() == null) {
                        throw new InvalidCommandArgument(); // TODO send message.
                    }
                    owner = owner == null ? context.getPlayer().getName() : owner;
                    final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(owner);
                    if (!offlinePlayer.hasPlayedBefore()) {
                        throw new InvalidCommandArgument(); // TODO send message
                    }
                    if (context.getPlayer() != null) {
                        return this.privateWarpManager.getWarp(offlinePlayer.getUniqueId(), warpName)
                                .map(warp -> warp.canBeAccessedBy(context.getPlayer()) ? warp : null)
                                .orElseThrow(InvalidCommandArgument::new);
                    }
                    return this.privateWarpManager.getWarp(warpName)
                            .orElseThrow(InvalidCommandArgument::new);
                });
    }

    @CommandAlias("warp")
    @CommandCompletion("@warps:accessible_only=true")
    @CommandPermission("Xiavic.player.warp")
    public void doWarp(@NotNull final Player player, final Warp warp) {
        if (!warp.canBeAccessedBy(player)) {
            Utils.sendMessage(player, warpMessages.messageWarpAccessDenied);
        } else if (!warp.isEnabled()) {
            Utils.sendMessage(player, warpMessages.messageWarpFailureDisabled);
        } else {
            warp.teleport(player).thenAccept(success -> {
                if (!success) {
                    Utils.sendMessage(player, warpMessages.messageWarpUnsafe);
                }
            });
        }
    }

    @CommandAlias("warp")
    @CommandCompletion("@warps:accessible_only=true @players")
    public void doWarp(@NotNull final Player sender, final Warp warp, final Player target) {
        if (!warp.canBeAccessedBy(target)) {
            Utils.sendMessage(target, warpMessages.messageWarpAccessDenied);
            Utils.sendMessage(sender, warpMessages.messageWarpFailureOther, "%reason%",
                    "Target insufficient permission.");
        } else if (!warp.isEnabled()) {
            Utils.sendMessage(target, warpMessages.messageWarpFailureDisabled);
            Utils.sendMessage(sender, warpMessages.messageWarpFailureOther, "%reason%",
                    warpMessages.messageWarpFailureDisabled.withoutColorCodes());
        } else {
            warp.teleport(target).thenAccept(success -> {
                if (!success) {
                    Utils.sendMessage(target, warpMessages.messageWarpUnsafe);
                    Utils.sendMessage(sender, warpMessages.messageWarpFailureOther,
                            warpMessages.messageWarpUnsafe.withoutColorCodes());
                } else {
                    Utils.sendMessage(sender, warpMessages.messageWarpTeleportSuccessOther,
                            "%player%", target.getDisplayName(), "%warp%", warp.getName());
                }
            });
        }
    }

    @CommandAlias("warpdebug")
    @CommandPermission("Xiavic.player.warps")
    public void showWarpsDebug(@NotNull final CommandSender sender) {
        for (Warp warp : privateWarpManager
                .getFilteredWarps((warp) -> warp.canBeAccessedBy(sender))) {
            final String name = warp.getName();
            final Location location = warp.getLocation();
            final String locationDisplay =
                    "World: " + location.getWorld() + " X: " + location.getBlockX() + " Y: " + location
                            .getBlockY() + " Z: " + location.getBlockZ();
            Utils
                    .sendMessage(sender, warpMessages.messagePrivateWarpListElementInfo, "%name%", name,
                            "%location%", locationDisplay, "%permission%", warp.getPermission(),
                            "%enabled%", String.valueOf(warp.isEnabled()));

        }
    }

    @CommandAlias("privatewarp|pw")
    @CommandCompletion("@private_warps:accessible_only=true")
    @CommandPermission("Xiavic.player.privatewarp")
    public void doPrivateWarp(@NotNull final Player player, @Optional final OfflinePlayer owner,
                              final PrivateWarp warp) {
        doWarp(player, warp);
    }

    @CommandAlias("warp")
    @CommandCompletion("@players @private_warps")
    public void doPrivateWarp(@NotNull final Player sender, @Optional final OfflinePlayer owner,
                              @NotNull final PrivateWarp warp, @NotNull final Player target) {
        doWarp(sender, warp, target);
    }

    @CommandAlias("privatewarpsdebug|pwd")
    public void showPrivateWarpsDebug(@NotNull final Player player) {
        showPrivateWarpsDebug(player, player);
    }

    @CommandAlias("privatewarpdebug|pwd")
    @CommandCompletion("@players")
    public void showPrivateWarpsDebug(@NotNull final CommandSender sender, Player owner) {
        Utils.sendMessage(sender, warpMessages.messagePrivateWarpListHeader);
        for (PrivateWarp warp : privateWarpManager.getFilteredWarps(
                (warp) -> warp.getOwner().equals(owner.getUniqueId()) && warp
                        .canBeAccessedBy(sender))) {
            final String name = warp.getName();
            final Location location = warp.getLocation();
            final String locationDisplay =
                    "World: " + location.getWorld() + " X: " + location.getBlockX() + " Y: " + location
                            .getBlockY() + " Z: " + location.getBlockZ();
            Utils
                    .sendMessage(sender, warpMessages.messagePrivateWarpListElementInfo, "%name%", name,
                            "%location%", locationDisplay, "%owner%", owner.getDisplayName(), "%enabled%",
                            String.valueOf(warp.isEnabled()));

        }
    }

    @CommandAlias("warps")
    @CommandPermission("Xiavic.player.warps")
    public void showWarps(@NotNull final CommandSender sender) {
        Utils.sendMessage(sender, warpMessages.messageWarpListHeader);
        final StringBuilder builder = new StringBuilder(",");
        warpManager.getWarps(warp -> warp.canBeAccessedBy(sender))
                .map((warp -> warp.isEnabled() ? "&a" + warp.getName() : "&c" + warp.getName()))
                .map(ChatColor::stripColor).sorted(Comparator.naturalOrder())
                .forEachOrdered(builder::append);
        Utils.chat(sender, builder.toString());
    }

    @CommandAlias("privatewarps|pws")
    @CommandPermission("Xiavic.player.privatewarps")
    public void showPrivateWarps(@NotNull final CommandSender sender,
                                 @NotNull final Player player) {
        Utils.sendMessage(sender, warpMessages.messagePrivateWarpListHeader);
        final StringBuilder builder = new StringBuilder(",");
        for (PrivateWarp privateWarp : privateWarpManager.getFilteredWarps(
                privateWarp -> privateWarp.canBeAccessedBy(sender) && privateWarp.getOwner()
                        .equals(player.getUniqueId()))) {
            builder.append(privateWarp.isEnabled() ?
                    "&a" :
                    "&c" + ChatColor.stripColor(privateWarp.getName()));
        }
        Utils.chat(sender, builder.toString());
    }

}
