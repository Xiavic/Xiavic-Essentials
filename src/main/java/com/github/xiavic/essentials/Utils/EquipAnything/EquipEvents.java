package com.github.xiavic.essentials.Utils.EquipAnything;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EquipEvents implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getWhoClicked();
        final ItemStack onCursor = player.getItemOnCursor();
        final ItemMeta meta = onCursor.getItemMeta();
        assert meta != null;
        if (onCursor.getType().isAir() || !meta.hasLore()) {
            return;
        }
        for (final String lore : meta.getLore()) {
            final ItemStack temp;
            final int slot;
            if ((lore.contains("helm")) && (event.getSlot() == 39)) {
                temp = player.getInventory().getItem(39);
                slot = 39;
            } else if ((lore.contains("chest")) && (event.getSlot() == 38)) {
                temp = player.getInventory().getItem(38);
                slot = 38;
            } else if ((lore.contains("legs")) && (event.getSlot() == 37)) {
                temp = player.getInventory().getItem(37);
                slot = 37;
            } else if ((lore.contains("feet")) && (event.getSlot() == 36)) {
                temp = player.getInventory().getItem(36);
                slot = 36;
            } else {
                continue;
            }
            player.getInventory().setItem(slot, player.getItemOnCursor());
            player.setItemOnCursor(temp == null ? null : temp.clone());
            player.updateInventory();
        }
    }
}
