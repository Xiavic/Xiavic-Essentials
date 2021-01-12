package com.github.xiavic.essentials.Commands.player.Fun.Links;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.LinkMessages;
import com.github.xiavic.essentials.Utils.messages.Messages;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LinksCommandHandler extends BaseCommand {

    private static final Messages messages = Messages.INSTANCE;
    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;
    private static final LinkMessages linkMessages = LinkMessages.INSTANCE;

    public LinksCommandHandler(@NotNull final BukkitCommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    @CommandAlias("discord")
    @CommandPermission("Xiavic.links.discord")
    public void showDiscord(@NotNull final CommandSender sender) {
        Utils.sendMessage(sender, linkMessages.messageDiscord);
    }

    @CommandAlias("forums")
    @CommandPermission("Xiavic.links.forums")
    public void showForums(@NotNull final CommandSender sender) {
        Utils.sendMessage(sender, linkMessages.messageForums);
    }

    @CommandAlias("website")
    @CommandPermission("Xiavic.links.website")
    public void showWebsite(@NotNull final CommandSender sender) {
        Utils.sendMessage(sender, linkMessages.messageWebsite);
    }

    @CommandAlias("twitter")
    @CommandPermission("Xiavic.links.twitter")
    public void showTwitter(@NotNull final CommandSender sender) {
        Utils.sendMessage(sender, linkMessages.messageTwitter);
    }

    @CommandAlias("youtube")
    @CommandPermission("Xiavic.links.youtube")
    public void showYoutube(@NotNull final CommandSender sender) {
        Utils.sendMessage(sender, linkMessages.messageYoutube);
    }


}
