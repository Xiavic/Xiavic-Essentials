package com.github.xiavic.essentials.Commands.staff.noncheat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("gamemode|gm") @CommandPermission("Xiavic.staff.gamemode")
public class GameModeCommandHandler extends BaseCommand {

    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;

    public GameModeCommandHandler(@NotNull final BukkitCommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @Default @Subcommand("creative|c|1") @CommandAlias("gmc")
    public void setGameModeCreative(final Player player) {
        player.setGameMode(GameMode.CREATIVE);
        Utils
            .sendMessage(player, commandMessages.messagePlayerChangeGamemode, "%mode%", "creative");
    }

    @Default @Subcommand("creative|c|1") @CommandAlias("gmc")
    public void setGameModeCreative(final CommandSender sender, final Player player) {
        setGameModeCreative(player);
        if (sender != player) {
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "creative");
        }
    }

    @Default @Subcommand("survival|s|0") @CommandAlias("gms")
    public void setGameModeSurvival(final Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        Utils
            .sendMessage(player, commandMessages.messagePlayerChangeGamemode, "%mode%", "survival");
    }

    @Default @Subcommand("survival|s|0") @CommandAlias("gms")
    public void setGameModeSurvival(final CommandSender sender, final Player player) {
        setGameModeSurvival(player);
        if (player != sender) {
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "survival");
        }
    }

    @Default @Subcommand("spectator|sp|3") @CommandAlias("gmsp")
    public void setGameModeSpectator(final Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        Utils.sendMessage(player, commandMessages.messagePlayerChangeGamemode, "%mode%",
            "spectator");
    }

    @Default @Subcommand("spectator|sp|3") @CommandAlias("gmsp")
    public void setGameModeSpectator(final CommandSender sender, final Player player) {
        setGameModeSpectator(player);
        if (player != sender) {
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "spectator");
        }
    }

    @Default @Subcommand("adventure|a|2") @CommandAlias("gma")
    public void setGameModeAdventure(final Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        Utils.sendMessage(player, commandMessages.messagePlayerChangeGamemode, "%mode%",
            "adventure");
    }

    @Default @Subcommand("adventure|a|2") @CommandAlias("gma")
    public void setGameModeAdventure(@NotNull final CommandSender sender,
        @NotNull final Player player) {
        setGameModeAdventure(player);
        if (player != sender) {
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "adventure");
        }
    }
}
