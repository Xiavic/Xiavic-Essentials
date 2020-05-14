package com.github.xiavic.essentials.Commands.UserCmds.Fun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrivateWarpCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
        @NotNull final String label, @NotNull final String[] args) {
        return false;
    }

    @Override public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
        @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        return null;
    }
}
