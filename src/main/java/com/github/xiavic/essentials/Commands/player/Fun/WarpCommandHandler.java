package com.github.xiavic.essentials.Commands.player.Fun;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.Messages;
import com.github.xiavic.essentials.Utils.warp.IPrivateWarpManager;
import com.github.xiavic.essentials.Utils.warp.IWarpManager;
import com.github.xiavic.essentials.Utils.warp.PrivateWarp;
import com.github.xiavic.essentials.Utils.warp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.stream.Collectors;

public class WarpCommandHandler extends BaseCommand {

    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;
    private static final Messages messages = Messages.INSTANCE;

    private final IPrivateWarpManager privateWarpManager;
    private final IWarpManager<Warp> warpManager;

    public WarpCommandHandler(@NotNull final BukkitCommandManager commandManager,
        @NotNull final IPrivateWarpManager pwManager,
        @NotNull final IWarpManager<Warp> warpManager) {
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

    @CommandAlias("warp") @CommandCompletion("@warps:accessible_only=true")
    @CommandPermission("Xiavic.player.warp")
    public void doWarp(@NotNull final Player player, final Warp warp) {
        warp.teleport(player).thenAccept(success -> {
            // TODO send message.
        });
    }

    @CommandAlias("warp") @CommandCompletion("@warps:accessible_only=true @players")
    public void doWarp(@NotNull final Player player, final Warp warp, final Player target) {
        doWarp(target, warp);
        if (player != target) {
            // TODO send message.
        }
    }

    @CommandAlias("warps") @CommandPermission("Xiavic.player.warps")
    public void showWarps(@NotNull final Player player) {
        // TODO send message.
    }

    @CommandAlias("privaewarp|pw") @CommandCompletion("@private_warps:accessible_only=true")
    @CommandPermission("Xiavic.player.privatewarp")
    public void doPrivateWarp(@NotNull final Player player, @Optional final OfflinePlayer owner,
        final PrivateWarp warp) {
        warp.teleport(player).thenAccept(success -> {
            // TODO send message.
        });
    }

    @CommandAlias("warp") @CommandCompletion("@players @private_warps")
    public void doPrivateWarp(@NotNull final Player player, @Optional final OfflinePlayer owner,
        @NotNull final PrivateWarp warp, @NotNull final Player target) {
        doPrivateWarp(target, owner, warp);
        if (player != target) {
            // TODO send message.
        }
    }

    @CommandAlias("privatewarps|pws") public void showPrivateWarps(@NotNull final Player player) {
        showPrivateWarps(player, player);
    }

    @CommandAlias("privatewarps|pws") @CommandCompletion("@players")
    public void showPrivateWarps(@NotNull final CommandSender sender, Player owner) {
        for (PrivateWarp warp : privateWarpManager.getFilteredWarps(
            (warp) -> warp.getOwner().equals(owner.getUniqueId()) && warp
                .canBeAccessedBy(sender))) {
            // TODO send message.
        }
    }

}
