package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import com.github.xiavic.essentials.Utils.warp.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WarpCommand implements TabExecutor {

    private static final WarpManager warpManager = WarpManager.INSTANCE;

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
        @NotNull final String label, @NotNull final String[] args) {
        return false;
    }

    @Nullable @Override public List<String> onTabComplete(@NotNull final CommandSender sender,
        @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        return null;
    }
}
