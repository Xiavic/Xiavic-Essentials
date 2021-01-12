package com.github.xiavic.essentials.Commands.player.Essential;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.CommandBooleanValue;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class RespawnHandler extends BaseCommand {

    private static final NamespacedKey VALUE_KEY =
            new NamespacedKey(Main.getPlugin(Main.class), "Instant_Respawn");

    private static final CommandMessages commandMessages = CommandMessages.INSTANCE;

    public RespawnHandler(@NotNull final BukkitCommandManager commandManager) {
        commandManager.registerCommand(this);
    }

    public boolean shouldInstantRespawn(@NotNull final Player player) {
        return player.getPersistentDataContainer()
                .getOrDefault(VALUE_KEY, PersistentDataType.BYTE, (byte) 0) == 1;
    }

    public void setInstantRespawn(@NotNull final Player player, final boolean respawn) {
        player.getPersistentDataContainer().set(VALUE_KEY, PersistentDataType.BYTE, (byte) (respawn ? 1 : 0));
    }

    @Default
    @CommandAlias("instantrespawn")
    @CommandPermission("Xiavic.player.instantrespawn")
    public void doInstantRespawnToggle(final Player player, @Optional CommandBooleanValue toggle) {
        if (toggle == null) {
            setInstantRespawn(player, !shouldInstantRespawn(player));
        } else {
            setInstantRespawn(player, toggle.value);
        }
        Utils.sendMessage(player, commandMessages.messageRespawnToggle, "%mode%", shouldInstantRespawn(player) ? "on" : "off");
    }


}
