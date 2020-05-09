package com.github.xiavic.essentials.Utils;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.TeleportHandler;
import com.github.xiavic.lib.teleport.ITeleportHandler;
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

import java.util.concurrent.TimeUnit;

public class Utils {

    private static ITeleportHandler teleportHandler;

    public static void loadTeleportHandler() {
        RegisteredServiceProvider<ITeleportHandler> rsp = Bukkit.getServicesManager().getRegistration(ITeleportHandler.class);
        if (rsp != null) {
            teleportHandler = rsp.getProvider();
        }
    }
    //EZ Chat Colors

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Sends messages to a player directly and makes the 'chat' name make more sense
    public static void chat(Player player, String string) {
        player.sendMessage(chat(string));
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
    public static boolean placeInCursorSlot(@NotNull final Player player, @Nullable final ItemStack itemStack) {
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

    @Deprecated
    public static void teleport(Player player, Location location) {
        teleportHandler.processPlayerTeleport(player);
        player.teleport(location);
    }

    // This teleport method lets you send a message to the player here instead of
    // having to do it where ever you called this method
    @Deprecated
    public static void teleport(Player player, Location location, String messagePath) {
        teleportHandler.processPlayerTeleport(player);
        player.teleport(location);
        chat(player, Main.messages.getString(messagePath));
    }


}
