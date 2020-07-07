package com.github.xiavic.essentials.Commands.staff.noncheat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.*;
import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused") public class StaffCommandHandler extends BaseCommand {

    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;
    private static final Messages messages = Messages.INSTANCE;

    public StaffCommandHandler(@NotNull final BukkitCommandManager commandManager) {
        commandManager.registerCommand(this);
        new GameModeCommandHandler(commandManager);
    }

    @CommandAlias("clear|ci") @CommandPermission("Xiavic.staff.clearothers")
    public void clearInventory(final Player player) {
        player.getInventory().clear();
        Utils.sendMessage(player, commandMessages.messageInventoryCleared);
    }

    @CommandAlias("clear|ci") @CommandPermission("Xiavic.staff.clearothers")
    @CommandCompletion("@players")
    public void clearInventory(final CommandSender sender, final Player player) {
        clearInventory(player);
        if (sender != player) {
            Utils.sendMessage(sender, commandMessages.messageInventoryClearedOther, "%target%",
                player.getDisplayName());
        }
    }

    @CommandAlias("coreconfigupdate|ccu") @CommandPermission("Xiavic.staff.config.update")
    public void reloadConfiguration(final CommandSender sender) {
        Main.mainConfig.forceReload();
        Main.messages.forceReload();
        Main.permissions.forceReload();
        Utils.sendMessage(sender, messages.messageConfigUpdated);
    }

    @CommandAlias("coreversion") @CommandPermission("Xiavic.staff.version")
    public void showVersion(final CommandSender sender) {
        Utils.sendMessage(sender, messages.messageShowPluginVersion, "%version%",
            Main.getPlugin(Main.class).getDescription().getVersion());
    }

    @CommandAlias("feed") @CommandPermission("Xiavic.staff.feed")
    public void feed(final Player player) {
        player.setFoodLevel(20);
        player.setSaturation(20);
        Utils.sendMessage(player, commandMessages.messagePlayerFed);
    }

    @CommandAlias("feed") @CommandPermission("Xiavic.staff.feedothers")
    @CommandCompletion("@players") public void feed(final CommandSender sender, Player target) {
        feed(target);
        if (sender != target) {
            Utils.sendMessage(sender, commandMessages.messagePlayerFedOther, "%target%",
                target.getDisplayName());
        }
    }

    @CommandAlias("setfirstspawn") @CommandPermission("Xiavic.staff.setfirstspawn")
    public void setFirstSpawn(final Player player) {
        final Location loc = player.getLocation();
        final World world = loc.getWorld();
        final double x = loc.getX(), y = loc.getY(), z = loc.getZ();
        final float yaw = loc.getYaw(), pitch = loc.getPitch();
        final String output =
            world.getName() + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
        Main.mainConfig.set("SpawnSystem.FirstSpawn", output);
        Utils.sendMessage(player, commandMessages.messageSetFirstJoinSpawnPoint);
    }

    @CommandAlias("fly") @CommandPermission("Xiavic.staff.fly")
    public void toggleFly(final Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        player.setFlying(!player.isFlying());
        Utils.sendMessage(player, commandMessages.messagePlayerFly, "%mode%",
            player.getAllowFlight() ? "&cenabled" : "&cdisabled");
    }

    @CommandAlias("fly") @CommandPermission("Xiavic.staff.flyothers") @CommandCompletion("@players")
    public void toggleFly(final CommandSender sender, final Player target) {
        toggleFly(target);
        Utils.sendMessage(sender, commandMessages.messagePlayerFlyOther, "%target%",
            target.getDisplayName(), "%mode%",
            target.getAllowFlight() ? "&cenabled" : "&cdisabled");
    }

    @CommandAlias("flyspeed") @CommandCompletion("1 2 3 4 5 6 7 8 9 10")
    @CommandPermission("Xiavic.staff.flyspeed")
    public void toggleFlySpeed(final Player player, int speed) {
        player.setFlySpeed(speed / 10f);
        Utils.sendMessage(player, commandMessages.messagePlayerChangeFlySpeed, "%amount%",
            String.valueOf(speed));
    }

    @CommandAlias("god") @CommandCompletion("true|false") @CommandPermission("Xiavic.staff.god")
    public void toggleGod(final Player player, @Optional Boolean enabled) {
        enabled = enabled == null ? !player.isInvulnerable() : enabled;
        player.setInvulnerable(enabled);
        Utils.sendMessage(player, commandMessages.messagePlayerChangeGodMode, "%mode%",
            enabled.toString());
    }

    @CommandAlias("god") @CommandCompletion("@players true|false")
    @CommandPermission("Xiavic.staff.godothers")
    public void toggleGod(final CommandSender sender, final Player player,
        @Optional final Boolean enabled) {
        toggleGod(player, enabled);
        Utils.sendMessage(sender, commandMessages.messagePlayerChangeGodModeOther, "%target%",
            player.getDisplayName(), "%mode%", String.valueOf(player.isInvulnerable()));
    }

    @CommandAlias("heal") @CommandPermission("Xiavic.staff.heal")
    public void doHeal(final Player player) {
        player.setHealth(20);
        player.setSaturation(20);
        player.setFoodLevel(20);
        Utils.sendMessage(player, commandMessages.messagePlayerHealed);
    }

    @CommandAlias("heal") @CommandPermission("Xiavic.staff.healothers")
    public void doHeal(final CommandSender sender, final Player player) {
        doHeal(player);
        Utils.sendMessage(sender, commandMessages.messagePlayerHealedOther, "%target%",
            player.getDisplayName());
    }

    @CommandAlias("healall") @CommandPermission("Xiavic.staff.healall")
    public void doMassHeal(final CommandSender sender) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            doHeal(player);
        }
        Utils.sendMessage(sender, commandMessages.messageAllPlayersHealed);
    }

    @CommandAlias("more") @CommandPermission("Xiavic.staff.more")
    public void giveMaxStack(final Player player) {
        final ItemStack inHand = player.getInventory().getItemInMainHand();
        final int maxSize = inHand.getMaxStackSize();
        inHand.setAmount(maxSize);
        player.getInventory().setItemInMainHand(inHand);
    }

    @CommandAlias("walkspeed") @CommandPermission("Xiavic.staff.walkspeed")
    @CommandCompletion("1 2 3 4 5 6 7 8 9 10")
    public void toggleWalkSpeed(final Player player, final int speed) {
        player.setWalkSpeed(speed / 10f);
        Utils.sendMessage(player, commandMessages.messagePlayerChangeWalkSpeed, "%amount%",
            String.valueOf(speed));
    }

    @CommandAlias("whois") @CommandPermission("Xiavic.player.realname")
    public void showRealName(final CommandSender sender, final Player player) {
        Utils.sendMessage(sender, commandMessages.messageWhoIsPlayer, "%nickname%",
            player.getDisplayName(), "%username%", player.getName());
        if (player.hasPermission(Main.permissions.getString("Whois"))) {
            final Location loc = player.getLocation();
            Utils.chat(player, "&6Player UUID: &9" + player.getUniqueId(),
                "&6Exp: &9" + player.getTotalExperience() + "&6, Next Level: &9" + player
                    .getExpToLevel(),
                "&6Health: &9" + player.getHealth() + "&6, Food: &9" + player.getFoodLevel(),
                "&6Time: &9" + player.getPlayerTime(),
                "&6Location: &9" + player.getWorld().getName().toUpperCase() + " | &cX &9" + loc
                    .getBlockX() + " | &cY &9" + loc.getBlockY() + " | &cZ &9" + loc.getBlockZ(),
                "&6Gamemode: &9" + player.getGameMode() + "&6, Can Fly: &9" + player
                    .getAllowFlight(),
                "&6First Joined: &9" + player.getFirstPlayed() + "&6, Last Played: &9" + player
                    .getLastSeen());
        }
    }

    @CommandAlias("gamemode|gm") @CommandPermission("Xiavic.staff.gamemode")
    public static class GameModeCommandHandler extends BaseCommand {

        public GameModeCommandHandler(@NotNull final BukkitCommandManager commandManager) {
            commandManager.registerCommand(this);
        }

        @Subcommand("creative|c|1") public void setGameModeCreative(final Player player) {
            player.setGameMode(GameMode.CREATIVE);
            Utils.sendMessage(player, commandMessages.messagePlayerChangeGamemode, "creative");
        }

        @Subcommand("creative|c|1")
        public void setGameModeCreative(final CommandSender sender, @NotNull final Player player) {
            setGameModeCreative(player);
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "creative");
        }

        @Subcommand("survival|s|0") public void setGameModeSurvival(final Player player) {
            player.setGameMode(GameMode.SURVIVAL);
            Utils.sendMessage(player, commandMessages.messagePlayerChangeGamemode, "%mode%",
                "survival");
        }

        @Subcommand("survival|s|0")
        public void setGameModeSurvival(final CommandSender sender, @NotNull final Player player) {
            setGameModeSurvival(player);
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "survival");
        }

        @Subcommand("spectator|sp|3") public void setGameModeSpectator(final Player player) {
            player.setGameMode(GameMode.SPECTATOR);
            Utils.sendMessage(player, commandMessages.messagePlayerChangeGamemode, "%mode%",
                "spectator");
        }

        @Subcommand("spectator|sp|3")
        public void setGameModeSpectator(final CommandSender sender, @NotNull final Player player) {
            setGameModeSpectator(player);
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "spectator");
        }

        @Subcommand("adventure|a|2") public void setGameModeAdventure(final Player player) {
            player.setGameMode(GameMode.ADVENTURE);
            Utils.sendMessage(player, commandMessages.messagePlayerChangeGamemode, "%mode%",
                "adventure");
        }

        @Subcommand("adventure|a|2")
        public void setGameModeAdventure(@NotNull final CommandSender sender,
            @NotNull final Player player) {
            setGameModeAdventure(player);
            Utils.sendMessage(sender, commandMessages.messagePlayerChangeGamemodeOther, "%target%",
                player.getDisplayName(), "%mode%", "adventure");
        }

    }

}
