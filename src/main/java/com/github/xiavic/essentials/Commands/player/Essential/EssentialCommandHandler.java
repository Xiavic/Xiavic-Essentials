package com.github.xiavic.essentials.Commands.player.Essential;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.*;
import com.github.xiavic.essentials.Commands.event.PlayerRepairItemEvent;
import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.CommandBooleanValue;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.Messages;
import com.github.xiavic.lib.NMSHandler.NMS;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused") public class EssentialCommandHandler extends BaseCommand {

    private static final Messages messages = Messages.INSTANCE;
    private static final NMS nms = Main.nmsImpl;
    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;

    public EssentialCommandHandler(@NotNull final BukkitCommandManager commandManager) {
        commandManager.registerCommand(this);
        new RespawnHandler(commandManager);
    }

    @Default @CommandAlias("cartography") @CommandPermission("Xiavic.player.cartography")
    public void openCartographyInventory(final Player player) {
        player.openInventory(Bukkit.createInventory(player, InventoryType.CARTOGRAPHY));
    }

    @Default @CommandAlias("dispose") @CommandPermission("Xiavic.player.dispose")
    public void openDisposalInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Chest");
        player.openInventory(inventory);
    }

    @Default @CommandAlias("enderchest|ec") @CommandPermission("Xiavic.player.ec")
    public void openEnderChest(final Player player) {
        player.openInventory(player.getEnderChest());
        Utils.sendMessage(player, commandMessages.messageEnderChestOpened);
    }

    @Default @CommandAlias("enderchest|ec") @CommandPermission("Xiavic.player.ec.others")
    @CommandCompletion("@players")
    public void openEnderChest(final Player sender, final Player target) {
        sender.openInventory(target.getEnderChest());
        Utils.sendMessage(sender, commandMessages.messageEnderChestOpenedOther, "%target%",
            target.getDisplayName());
    }

    @Default @CommandAlias("extinguish|ext") @CommandPermission("Xiavic.player.extinguish")
    public void doExtinguish(final Player sender) {
        sender.setFireTicks(0);
        Utils.sendMessage(sender, commandMessages.messagePlayerExtinguishedSelf);
    }

    @Default @CommandAlias("extinguish|ext") @CommandPermission("Xiavic.staff.extinguishothers")
    public void doExtinguish(final CommandSender sender, final String... players) {
        boolean nullPlayer = false;
        for (String rawPlayer : players) {
            final Player player = Bukkit.getPlayer(rawPlayer);
            if (player == null) {
                nullPlayer = true;
                continue;
            }
            player.setFireTicks(0);
            if (sender instanceof Player) {
                Utils
                    .sendMessage(sender, commandMessages.messagePlayerExtinguishedOther, "%sender%",
                        ((Player) sender).getDisplayName());
            }
        }
        if (nullPlayer) {
            Utils.sendMessage(sender, messages.messageVarArgPlayerNotFound);
        }
    }

    @Default @CommandAlias("grindstone") @CommandPermission("Xiavic.player.grindstone")
    public void openGrindstoneInventory(final Player player) {
        player.openInventory(Bukkit.createInventory(player, InventoryType.GRINDSTONE));
    }

    @Default @CommandAlias("loom") @CommandPermission("Xiavic.player.loom")
    public void openLoomInventory(final Player player) {
        player.openInventory(Bukkit.createInventory(player, InventoryType.LOOM));
    }

    @Default @CommandAlias("repair") @CommandPermission("Xiavic.player.repair")
    public void doItemRepair(final Player player) {
        final ItemStack inHand = player.getInventory().getItemInMainHand();
        if (inHand.getType().isAir()) {
            Utils.sendMessage(player, messages.messageItemIsAir);
            return;
        }
        final ItemMeta meta = inHand.getItemMeta();
        if (meta instanceof Damageable) {
            PlayerRepairItemEvent event =
                new PlayerRepairItemEvent(player, player.getInventory().getItemInMainHand());
            Bukkit.getPluginManager().callEvent(event);
            final Damageable damageable = (Damageable) event.getItemStack().getItemMeta();
            if (!event.isCancelled() && damageable != null) {
                damageable.setDamage(0);
                Utils.sendMessage(player, messages.messageItemRepaired);
            } //If item repair event was cancelled, don't send a message.
        } else {
            Utils.sendMessage(player, messages.messageItemRepairFailure);
        }
    }

    @Default @CommandAlias("signedit") @CommandPermission("Xiavic.player.signedit")
    public void openSignEditor(final Player player) {
        List<Block> block = player.getLineOfSight(Collections.emptySet(), 4);
        if (block.isEmpty()) {
            Utils.sendMessage(player, messages.messageLookAtSign);
            return;
        }
        Block first = block.get(0);
        BlockState blockState = first.getState();
        if (first instanceof Sign) {
            nms.getSignEditor().openUI(player, (Sign) first);
        } else {
            toggleDynamicSignEdit(player, CommandBooleanValue
                .fromBoolean(!nms.getSignEditor().isDynamicEditingEnabled(player.getUniqueId())));
        }
    }

    @Default @CommandAlias("signedit") @CommandPermission("Xiavic.player.signedit")
    @CommandCompletion("@toggles")
    public void toggleDynamicSignEdit(final Player player, final CommandBooleanValue value) {
        nms.getSignEditor().toggleDynamicEditing(player.getUniqueId(), value.value);
        Utils.sendMessage(player , commandMessages.messageDynamicSignEditToggled, "%state%", String.valueOf(value.value));
    }

    @Default @CommandAlias("signedit") @CommandPermission("Xiavic.player.signedit")
    @CommandCompletion("1|2|3|4")
    public void editSign(final Player player, final int lineNumber, String... args) {
        List<Block> block = player.getLineOfSight(Collections.emptySet(), 4);
        if (lineNumber < 1 || lineNumber > 4) {
            Utils.sendMessage(player, messages.messageSignTooManyLines, "%lines%", String.valueOf(lineNumber));
            return;
        }
        final Block first = block.get(0);

        final BlockState blockState = first.getState();
        if (!(first instanceof Sign)) {
            Utils.sendMessage(player, messages.messageLookAtSign);
            return;
        }
        final Sign sign = (Sign) blockState;
        if (args.length > 0) {
            args = Arrays.copyOfRange(args, 1, args.length);
        }
        //Parse sign input
        List<String> lines = new LinkedList<>();
        StringBuilder builder = new StringBuilder();
        for (final String str : args) {
            if (str.contains(";;")) {
                lines.addAll(Arrays.asList(str.split(";;")));
                continue;
            }
            builder.append(str).append(" ");
        }
        final String str = builder.toString();
        if (!str.isEmpty()) {
            lines.add(str);
        }
        int index = lineNumber;
        for (String s : lines) { //Set the individual lines
            sign.setLine(index++, s);
        }
        sign.update(true);
        Utils.sendMessage(player, commandMessages.messageSignEdited);
    }

    @Default @CommandAlias("stonecutter") @CommandPermission("Xiavic.player.stonecutter")
    public void openGrindstone(final Player player) {
        player.openInventory(Bukkit.createInventory(player, InventoryType.STONECUTTER));
    }

    @Default @CommandAlias("suicide") @CommandPermission("Xiavic.player.suicide")
    public void commitSuicide(final Player player) {
        player.setHealth(0);
    }

    @Default @CommandAlias("workbench|wb") @CommandPermission("Xiavic.player.workbench")
    public void openWorkbench(final Player player) {
        player.openWorkbench(player.getLocation(), false);
    }
}
