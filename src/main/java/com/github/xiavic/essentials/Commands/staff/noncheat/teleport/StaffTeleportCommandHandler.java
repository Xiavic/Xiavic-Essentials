package com.github.xiavic.essentials.Commands.staff.noncheat.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.TeleportationMessages;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused") public class StaffTeleportCommandHandler extends BaseCommand {

    private static final TeleportationMessages teleportationMessages =
        TeleportationMessages.INSTANCE;

    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;

    private final ITeleportHandler teleportHandler;

    public StaffTeleportCommandHandler(@NotNull final BukkitCommandManager commandManager,
        @NotNull final ITeleportHandler teleportHandler) {
        commandManager.registerCommand(this);
        this.teleportHandler = teleportHandler;
    }

    @CommandAlias("teleport|tp") @CommandPermission("Xiavic.staff.tp")
    @CommandCompletion("@players")
    public void doTeleport(final Player sender, final Player target) {
        teleportHandler.teleport(sender, target, false);
        Utils.sendMessage(sender, teleportationMessages.messageTeleported, "has", "have",
            "%target1%", "You", "%target2%", target.getDisplayName());
    }

    @CommandAlias("teleport|tp") @CommandPermission("Xiavic.staff.tp.other")
    public void doTeleport(final CommandSender sender, final Player toTeleport,
        final Player target) {
        switch (teleportHandler.remoteTp(toTeleport, target)) {
            case 0:
                Utils.sendMessage(sender, teleportationMessages.messageTeleported, "%target1%",
                    toTeleport.getDisplayName(), "%target2%", target.getDisplayName());
                break;
            case 1:
                Utils.sendMessage(sender, teleportationMessages.messageTeleportationDisabled,
                    "%target%", toTeleport.getDisplayName());
                break;
            case 2:
                Utils.sendMessage(sender, teleportationMessages.messageTeleportationDisabled,
                    "%target%", target.getDisplayName());
        }
    }

    @CommandAlias("teleportall|tpall") @CommandPermission("Xiavic.staff.tpall")
    public void doMassTeleport(final Player sender) {
        // TODO send mesage
        long immunity = Main.mainConfig.getLong("TeleportAll.Immunity");
        if (immunity != 0) {
            sender.setInvulnerable(true);
        }
        final boolean senderInvulnerable = sender.isInvulnerable();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p == sender) {
                continue;
            }
            final boolean invulnerable = sender.isInvulnerable();
            if (immunity != 0) {
                sender.setInvulnerable(true);
            }
            Utils.sendMessage(p, teleportationMessages.messageForceTeleported);
            teleportHandler.teleport(sender, p, false).thenAccept((unused) -> Bukkit.getScheduler()
                .runTaskLater(Main.getPlugin(Main.class), () -> p.setInvulnerable(invulnerable),
                    Utils.toTicks(immunity, TimeUnit.SECONDS)));
        }
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class),
            () -> sender.setInvulnerable(senderInvulnerable),
            Utils.toTicks(immunity, TimeUnit.SECONDS)); //Revert invulnerability
    }

    @CommandAlias("teleporthere|tphere") @CommandPermission("Xiavic.staff.tphere")
    public void doTeleportHere(final Player sender, final Player toTeleport) {
        Utils.sendMessage(toTeleport, teleportationMessages.messageForceTeleported);
        teleportHandler.teleport(toTeleport, sender, false);
    }

    @Subcommand("teleportposition|tppos") @CommandPermission("Xiavic.staff.tppos")
    public void doTeleportPosition(final Player sender, final double x, final double y,
        final double z) {
        doTeleportPosition(sender, sender.getWorld(), x, y, z);
    }

    @Subcommand("teleportposition|tppos") @CommandPermission("Xiavic.staff.tppos")
    @CommandCompletion("@worlds")
    public void doTeleportPosition(final Player sender, final World world, final double x,
        final double y, final double z) {
        teleportHandler.teleport(sender, new Location(world, x, y, z));
    }
}
