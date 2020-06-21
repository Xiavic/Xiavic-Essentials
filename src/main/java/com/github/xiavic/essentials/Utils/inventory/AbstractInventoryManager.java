package com.github.xiavic.essentials.Utils.inventory;

import com.github.xiavic.lib.inventory.InventorySerializer;
import com.github.xiavic.lib.serialization.DataAccessObject;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class AbstractInventoryManager {

    @NotNull
    private final DataAccessObject database;
    @NotNull
    private final InventorySerializer serializer;

    public AbstractInventoryManager(@NotNull final DataAccessObject database, @NotNull final InventorySerializer serializer) {
        this.database = database;
        this.serializer = serializer;
    }

    public DataAccessObject getDatabase() {
        return database;
    }

    public void saveInventory(@NotNull final String key, @NotNull final PlayerInventory inventory) {
        byte[] binary = serializer.serialize(inventory);
        database.save(key, binary);
    }
}
