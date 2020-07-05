package com.github.xiavic.essentials.Commands.player.Fun;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Listeners.AFKHandler;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.Message;
import com.github.xiavic.essentials.Utils.messages.Messages;
import com.github.xiavic.lib.NMSHandler.NMSVersion;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.banner.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class FunCommandHandler extends BaseCommand {

    private static final Messages messages = Messages.INSTANCE;
    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;

    private final ITeleportHandler teleportHandler;

    public FunCommandHandler(@NotNull final BukkitCommandManager commandManager,
        @NotNull final ITeleportHandler teleportHandler) {
        commandManager.registerCommand(this);
        this.teleportHandler = teleportHandler;
    }

    @CommandAlias("afk") @CommandPermission("Xiavic.player.afk")
    public void toggleAFK(@NotNull final Player player) {
        final AFKHandler handler = AFKHandler.INSTANCE;
        handler.setAFK(player.getUniqueId(), !handler.isAFK(player.getUniqueId()));
    }

    @CommandAlias("argh") @CommandPermission("Xiavic.player.argh")
    public void sendArgh(@NotNull final Player player) {
        Utils.sendMessage(player, commandMessages.messagePlayerArgh);
    }

    @CommandAlias("coinflip|cf") @CommandPermission("Xiavic.player.coinflip")
    public void doCoinflip(@NotNull final Player player) {
        final int random = ThreadLocalRandom.current().nextInt(0, 1);
        final Message message = random == 0 ? messages.messageHeads : messages.messageTails;
        Utils.sendMessage(player, message);
    }

    @CommandAlias("hat") @CommandPermission("Xiavic.player.hat")
    public void doHat(@NotNull final Player player) {
        final PlayerInventory playerInventory = player.getInventory();
        final ItemStack inHand = playerInventory.getItemInMainHand().clone();
        if (inHand.getType().isAir()) {
            Utils.sendMessage(player, messages.messageItemIsAir);
            return;
        }
        final ItemStack onHead = playerInventory.getHelmet() == null ?
            new ItemStack(Material.AIR) :
            playerInventory.getHelmet();
        playerInventory.setItemInMainHand(onHead);
        playerInventory.setHelmet(inHand);
    }

    @CommandAlias("head") @CommandPermission("Xiavic.player.head") @SuppressWarnings("deprecation")
    public void giveHead(@NotNull final Player sender, @NotNull final String offlinePlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(Main.class), () -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(offlinePlayer);
            final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            assert skullMeta != null;
            skullMeta.setOwningPlayer(player);
            Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> {
                if (!Utils.placeInCursorSlot(sender, itemStack)) {
                    Utils.sendMessage(sender, messages.messageInventoryFull);
                }
            });
        });
    }

    @CommandAlias("iteminfo|ii") @CommandPermission("Xiavic.player.iteminfo")
    public void showItemInfo(@NotNull final Player player) {
        Utils.chat(player, " ");
        final PlayerInventory inventory = player.getInventory();
        final ItemStack inHand = inventory.getItemInMainHand();
        if (inHand.getType().isAir()) {
            Utils.sendMessage(player, messages.messageItemIsAir);
            return;
        }
        final ItemMeta meta = inHand.getItemMeta();
        assert meta != null;
        if (meta.hasDisplayName()) {
            Utils.chat(player, "&eItem Meta: &b" + meta.getDisplayName());
        }

        Utils.chat(player, "&eItem Type Name: &b" + inHand.getType().name());
        Utils.chat(player, "&eMax Stack: &b" + inHand.getMaxStackSize());
        if (!inHand.getEnchantments().isEmpty()) {
            Utils.chat(player, "&eItem Enchantments: &c ====================================");
            final Map<Enchantment, Integer> ench =
                player.getInventory().getItemInMainHand().getEnchantments();
            for (Map.Entry<Enchantment, Integer> entry : ench.entrySet()) {
                String str = entry.getKey().getKey().getKey().replace("_", " ");
                String name = Utils.titleCase(" ", str);
                int level = entry.getValue();
                String output = "      &b" + name + " : " + level;
                Utils.chat(player, output);
            }
        }
        Utils.chat(player, " ", "&aItem Specific Information: ");
        if (meta instanceof BookMeta) {
            showBookInfo(player, (BookMeta) meta);
        }
        if (meta instanceof SkullMeta) {
            showSkullInfo(player, (SkullMeta) meta);
        }
        if (!PaperLib.isPaper()) {
            return;
        }
        //Paper Specific information
        if (meta instanceof ArmorStandMeta) {
            showArmorStandInfo(player, (ArmorStandMeta) meta);
        }
        if (meta instanceof BannerMeta) {
            showBannerInfo(player, (BannerMeta) meta);
        }

        if (meta instanceof CompassMeta) {
            showCompassInfo(player, (CompassMeta) meta);
        }

        if (meta instanceof BlockStateMeta) {
            showBlockStateInfo(player, (BlockStateMeta) meta);
        }

        if (meta instanceof BlockDataMeta) {
            showBlockDataInfo(player, (BlockDataMeta) meta, inHand.getType().createBlockData()
                .getMaterial()); //Get the material of a "wood be" block data.
        }
    }

    @CommandAlias("nearby") @CommandPermission("Xiavic.player.nearby")
    public void showNearbyPlayers(@NotNull final Player player) {
        final double radius = Main.mainConfig.getDouble("NearRadius");
        List<String> nearbyPlayers = Bukkit.getOnlinePlayers().stream().filter(target -> {
            double distance = player.getLocation().distance(target.getLocation());
            return distance <= radius;
        }).map(target -> "    " + target.getName() + ": " + player.getLocation()
            .distance(target.getLocation()) + "m").collect(Collectors.toList());
        if (!nearbyPlayers.isEmpty()) {
            Utils.chat(player, "List of nearby players:");
            for (String s : nearbyPlayers) {
                Utils.chat(player, s);
            }
        }
    }

    @CommandAlias("top") @CommandPermission("Xiavic.player.top")
    public void goTop(@NotNull final Player player) {
        final Location highestBlock =
            player.getWorld().getHighestBlockAt(player.getLocation()).getLocation();
        final Location current = player.getLocation();
        highestBlock.setPitch(current.getPitch());
        highestBlock.setYaw(current.getYaw());
        highestBlock.setDirection(current.getDirection());

    }

    private void showSkullInfo(@NotNull final CommandSender sender,
        @NotNull final SkullMeta skullMeta) {
        if (skullMeta.hasOwner()) {
            Utils.chat(sender, "Owner " + skullMeta.getOwningPlayer().getName());
        }
    }

    private void showBookInfo(@NotNull final CommandSender sender,
        @NotNull final BookMeta bookMeta) {
        final BookMeta.Generation generation = bookMeta.getGeneration();
        if (bookMeta.hasTitle()) {
            Utils.chat(sender, "&3Title: &b" + bookMeta.getTitle());
        }
        if (bookMeta.hasAuthor()) {
            Utils.chat(sender, "&3Author: &b" + bookMeta.getAuthor());
        }
        Utils.chat(sender, "&4Page Count: &b" + bookMeta.getPageCount());
        if (generation != null) {
            Utils.chat(sender, "&3Book Generation: &b" + Utils
                .titleCase(" ", generation.name().toLowerCase().replaceAll("_", " ")));
        }
    }

    private void showBannerInfo(@NotNull final CommandSender sender,
        @NotNull final BannerMeta bannerMeta) {
        List<Pattern> patterns = bannerMeta.getPatterns();
        Utils.chat(sender, "&3Patterns: ");
        for (final Pattern pattern : patterns) {
            String display = "&3Pattern Name: &b";
            display = display
                .concat(Utils.titleCase(" ", pattern.getPattern().name().replaceAll("_", " ")));
            display = display.concat(" " + "&3Color: &b" + Utils
                .titleCase(" ", pattern.getColor().name().replaceAll("_", " ")));
            Utils.chat(sender, display);
        }
    }

    private void showArmorStandInfo(@NotNull final CommandSender sender,
        @NotNull final ArmorStandMeta armorStandMeta) {
        Utils.chat(sender, "&3Has base plate: &b" + !armorStandMeta.hasNoBasePlate(),
            "&3Small: &b" + armorStandMeta.isSmall(),
            "&3Invisible: &b" + armorStandMeta.isInvisible(),
            "&3Marker: &b" + armorStandMeta.isMarker(),
            "&3Show arms: &b" + armorStandMeta.shouldShowArms());
    }

    private void showCompassInfo(@NotNull final CommandSender sender,
        @NotNull final CompassMeta compassMeta) {
        if (NMSVersion.getCurrent().isNewerThan(NMSVersion.V1_15_R1)) { //So if 1.16+
            Utils.chat(sender, "&3Loadstone Tracked: &b" + compassMeta.isLodestoneTracked(),
                "&3Loadstone Location: &b", compassMeta.hasLodestone() ?
                    compassMeta.getLodestone().toString() :
                    "No loadstone found.");
        }
    }

    private void showBlockStateInfo(@NotNull final CommandSender sender,
        @NotNull final BlockStateMeta blockStateMeta) {
        if (blockStateMeta.hasBlockState()) {
            Utils.chat(sender,
                "&3Raw Block State Info: &b" + blockStateMeta.getBlockState().toString());
        } else {
            Utils.chat(sender, "&3BRaw Block State Info: &bNo Block State Found.");
        }
    }

    private void showBlockDataInfo(@NotNull final CommandSender sender,
        @NotNull final BlockDataMeta blockDataMeta, @NotNull final Material material) {
        if (blockDataMeta.hasBlockData()) {
            Utils.chat(sender,
                "&3Raw Block Data Info: &b" + blockDataMeta.getBlockData(material).toString());
        } else {
            Utils.chat(sender, "&3Raw Block Data Info: &bNo Block Data Found.");
        }
    }
}
