package com.github.xiavic.essentials.Commands.player.Essential.Teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import com.github.xiavic.essentials.Commands.messages.TeleportationMessages;
import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.LocationUtils;
import com.github.xiavic.essentials.Utils.Tpa.TpaHandler;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.Messages;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class TeleportationCommandHandler extends BaseCommand {

    private static final Messages messages = Messages.INSTANCE;
    private static final TeleportationMessages tpMessages = TeleportationMessages.INSTANCE;
    private final Main plugin;
    private final ITeleportHandler teleportHandler;
    private final TpaHandler tpaHandler;

    public TeleportationCommandHandler(@NotNull final BukkitCommandManager commandManager,
        @NotNull final ITeleportHandler teleportHandler, @NotNull final TpaHandler tpaHandler) {
        commandManager.registerCommand(this);
        this.plugin = (Main) commandManager.getPlugin();
        this.teleportHandler = teleportHandler;
        this.tpaHandler = tpaHandler;
    }

    @CommandAlias("back") @CommandPermission("Xiavic.player.back")
    public void doBack(final Player player) {
        final Location last = teleportHandler.getLastLocation(player);
        if (last != null) {
            teleportHandler.teleport(player, last);
            Utils.sendMessage(player, tpMessages.messageTeleportingLastLocation);
        } else {
            Utils.sendMessage(player, tpMessages.messageNoPreviousLocation);
        }
    }

    @CommandAlias("randomteleport|rtp") @CommandPermission("Xiavic.player.rtp")
    public void doRandomTeleport(final Player player) {
        final double distance = plugin.getConfig().getDouble("RTPDistance");
        double randomX = (ThreadLocalRandom.current().nextDouble() * (distance * 2)) - distance;
        double randomZ = (ThreadLocalRandom.current().nextDouble() * (distance * 2)) - distance;
        double randomY = player.getWorld()
            .getHighestBlockYAt((int) Math.round(randomX), (int) Math.round(randomZ)) + 1.5;
        Location rtp = new Location(player.getWorld(), randomX, randomY, randomZ);
        teleportHandler.teleport(player, rtp);
        Utils.sendMessage(player, tpMessages.messageTeleportedRandomly);
        Block block = rtp.getBlock().getRelative(0, -1, 0);
        if (block.getType().equals(Material.WATER) || block.getType().equals(Material.LAVA)) {
            block.setType(Material.DIRT);
        }
    }

    @CommandAlias("spawn") @CommandPermission("Xiavic.player.spawn")
    public void doSpawnTeleport(final Player player) {
        final Location spawn = LocationUtils.getLocation("SpawnSystem.Spawn");
        teleportHandler.teleport(player, spawn);
        Utils.sendMessage(player, tpMessages.messageTeleported, "has", "have", "%target1%", "You",
            "%target2%", "Spawn");
    }

    @CommandAlias("spawn") @CommandPermission("Xiavic.staff.spawnothers")
    @CommandCompletion("vararg_players:pop=0")
    public void doSpawnTeleport(final CommandSender sender, String... players) {
        final Location spawn = LocationUtils.getLocation("SpawnSystem.Spawn");
        for (String arg : players) {
            Player target = Bukkit.getPlayer(arg);
            if (target != null) {
                teleportHandler.teleport(target, spawn);
                Utils.sendMessage(sender, tpMessages.messageTeleported, "%target1%", arg,
                    "%target2%", "Spawn");
                if (sender != target) {
                    Utils.sendMessage(target, tpMessages.messageTeleported, "has", "have",
                        "%target1%", "You", "%target2%", "Spawn");
                }
            } else {
                Utils.sendMessage(sender, messages.messageVarArgPlayerNotFound, "%player%", arg);
            }
        }
    }

    @CommandAlias("tpa") @CommandPermission("Xiavic.player.tpa") @CommandCompletion("@players")
    public void doTeleportRequest(final Player sender, final Player target) {
        if (sender == target) {
            Utils.sendMessage(sender, tpMessages.messageTeleportSelf);
            return;
        }
        final int result = tpaHandler.addRequest(sender, target);
        Utils.sendMessage(sender, tpMessages.messageTeleportRequestSent, "%target%",
            target.getDisplayName());
        if (result == 2) {
            Utils.sendMessage(sender, tpMessages.messageTeleportationDisabled, "%target%",
                target.getDisplayName());
        } else { //If the result was a success
            Utils.sendMessage(target, tpMessages.messageTeleportRequestReceived, "%sender%", sender.getDisplayName());
        }
    }

    @CommandAlias("tpaccept") @CommandPermission("Xiavic.player.tpaccept")
    public void acceptTeleportRequest(final Player sender) {
        tpaHandler.parseRequest(sender, true);
    }

    @CommandAlias("tpdeny") @CommandPermission("Xiavic.player.tpdeny")
    public void denyTeleportRequest(final Player sender) {
        tpaHandler.parseRequest(sender, false);
    }
}
