package com.github.xiavic.essentials.Utils;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.messages.Message;
import com.github.xiavic.essentials.Utils.messages.Messages;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import me.minidigger.minimessage.bungee.MiniMessageParser;
import me.minidigger.minimessage.bungee.MiniMessageSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static final Messages messages = Messages.INSTANCE;
    private static ITeleportHandler teleportHandler;

    public static void loadTeleportHandler() {
        RegisteredServiceProvider<ITeleportHandler> rsp =
            Bukkit.getServicesManager().getRegistration(ITeleportHandler.class);
        if (rsp != null) {
            teleportHandler = rsp.getProvider();
        }
    }
    //EZ Chat Colors

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Sends messages to a player directly and makes the 'chat' name make more sense
    public static void chat(CommandSender player, String... strings) {
        for (String string : strings) {
            player.sendMessage(chat(string));
        }
    }

    public static String capitalize(@NotNull final String s) {
        if (s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    };

    public static String titleCase(@NotNull final String delimiter, @NotNull final String s) {
        if (s.isEmpty()) {
            return s;
        }
        final String[] split = s.split(delimiter);
        if (split.length == 0) {
            return s;
        }
        final StringBuilder builder = new StringBuilder(delimiter);
        for (String s1 : split) {
            builder.append(capitalize(s1));
        }
        return builder.toString();
    }

    /**
     * Send a message to a recipient.
     * Taken from: https://github.com/Sauilitired/Hyperverse
     *
     * @param recipient    Receiver of the message
     * @param message      Message to send
     * @param replacements Replacements
     * @see #format(String, String...) for information about string replacements
     */
    public static void sendPrefixedMessage(@NotNull final CommandSender recipient,
        @NotNull final Message message, @NotNull final String... replacements) {
        Objects.requireNonNull(recipient);
        final String replacedMessage = format(message.toString(), replacements);
        if (replacedMessage.isEmpty()) {
            return;
        }
        if (replacedMessage.contains("<") && replacedMessage.contains(">")) {
            if (replacedMessage.contains(ChatColor.COLOR_CHAR + "")) {
                final String prefixedMessage = ChatColor.translateAlternateColorCodes('&',
                    messages.messagePrefix.toString() + replacedMessage);
                final String fixedMessage =
                   MiniMessageSerializer.serialize(TextComponent.fromLegacyText(prefixedMessage));
                recipient.spigot().sendMessage(MiniMessageParser.parseFormat(fixedMessage));
            } else {
                final String prefixedMessage = messages.messagePrefix.toString() + replacedMessage;
                recipient.spigot().sendMessage(MiniMessageParser.parseFormat(prefixedMessage));
            }
        } else {
            final String prefixedMessage = ChatColor.translateAlternateColorCodes('&',
                messages.messagePrefix.toString() + replacedMessage);
            recipient.sendMessage(prefixedMessage);
        }
    }

    public static void sendMessage(@NotNull final CommandSender recipient,
        @NotNull final Message message, @NotNull final String... replacements) {
        Objects.requireNonNull(recipient);
        final String replacedMessage = format(message.toString(), replacements);
        if (replacedMessage.isEmpty()) {
            return;
        }
        if (replacedMessage.contains("<") && replacedMessage.contains(">")) {
            if (replacedMessage.contains(ChatColor.COLOR_CHAR + "")) {
                final String fixedMessage =
                    MiniMessageSerializer.serialize(TextComponent.fromLegacyText(replacedMessage));
                recipient.spigot().sendMessage(MiniMessageParser.parseFormat(fixedMessage));
            } else {
                recipient.spigot().sendMessage(MiniMessageParser.parseFormat(replacedMessage));
            }
        } else {
            final String prefixedMessage = ChatColor.translateAlternateColorCodes('&',
                messages.messagePrefix.toString() + replacedMessage);
            recipient.sendMessage(prefixedMessage);
        }
    }

    /**
     * Format a string. Replacements come in pairs of two, where
     * the first value is the string to be replaced, and the second
     * value is the replacement, example:
     * %key1%, value1, %key2%, value2
     * Taken from: https://github.com/Sauilitired/Hyperverse
     *
     * @param message      String to format
     * @param replacements Replacements, needs to be a multiple of 2
     * @return The formatted string
     */
    @NotNull public static String format(@NotNull final String message,
        @NotNull final String... replacements) {
        if (replacements.length % 2 != 0) {
            throw new IllegalArgumentException("Replacement length must be a multiple of two");
        }
        String replacedMessage = Objects.requireNonNull(message);
        for (int i = 0; i < replacements.length; i += 2) {
            replacedMessage = replacedMessage.replace(replacements[i], replacements[i + 1]);
        }
        return replacedMessage;
    }

    // An overload so you can do the same thing when you need to send a message to console
    // from inside a command class
    public static void chat(CommandSender sender, String string) {
        sender.sendMessage(chat(string));
    }

    /**
     * Attempt to replace the currently held item to a new one. The old item will be moved to
     * the next empty slot.
     *
     * @param player    The player's {@link Player} instance.
     * @param itemStack The {@link ItemStack} to replace - if null, air will be set instead.
     * @return Returns whether or not the swap was successful.
     */
    public static boolean placeInCursorSlot(@NotNull final Player player,
        @Nullable final ItemStack itemStack) {
        final PlayerInventory inventory = player.getInventory();
        final int firstEmpty = inventory.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        }
        final int held = inventory.getHeldItemSlot();
        inventory.setItem(firstEmpty, inventory.getItem(held));
        inventory.setItem(held, itemStack == null ? null : itemStack.clone());
        return true;
    }

    public static long toTicks(final long duration, final TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(duration, timeUnit) * 50; //Each tick is 50ms
    }

    public static long fromTicks(final long ticks, final TimeUnit timeUnit) {
        return timeUnit.convert(ticks / 50, timeUnit);
    }

    public static String parseNMSVersion() {
        final Server server = Bukkit.getServer();
        return server.getClass().getPackage().getName().replace("org.bukkit.craftbukkit", "");
    }

    @Deprecated public static void teleport(Player player, Location location) {
        teleportHandler.processPlayerTeleport(player);
        player.teleport(location);
    }

    // This teleport method lets you send a message to the player here instead of
    // having to do it where ever you called this method
    @Deprecated public static void teleport(Player player, Location location, String messagePath) {
        teleportHandler.processPlayerTeleport(player);
        player.teleport(location);
        chat(player, Main.messages.getString(messagePath));
    }
}
