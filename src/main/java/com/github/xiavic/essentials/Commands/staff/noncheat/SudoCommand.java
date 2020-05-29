package com.github.xiavic.essentials.Commands.staff.noncheat;

import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.xiavic.essentials.Main.messages;
import static com.github.xiavic.essentials.Main.permissions;

/**
 * Represents a command for sudo.
 */
public class SudoCommand implements TabExecutor {

    @Override //Target: /sudo <player> <command> [args]
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
                             @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission(permissions.getString("Sudo")) || !sender.isOp()) {
            Utils.chat(sender, messages.getString("NoPerms"));
            return true;
        }
        if (args.length < 2) {
            return false;
        }
        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            Utils.chat(sender, messages.getString("PlayerNotFound"));
            return true;
        }
        if (args[1].startsWith("s:")) { //Send chat message as the player.
            StringBuilder builder = new StringBuilder();
            String str = args[1].replace("s:", "");
            builder.append(str).append(" ");
            for (int index = 2; index < args.length; ) {
                builder.append(args[index++]);
            }
            player.chat(builder.toString()); //TODO Needs testing
            return true;
        }
        final Command target = Bukkit.getPluginCommand(args[1]);
        if (target == null) {
            Utils.chat(player, messages.getString("NoSuchCommand"));
            return true;
        }
        target.execute(player, args[0], Arrays.copyOfRange(args, 1, args.length - 1));
        return true;
    }

    /**
     * Tab completer for sudo.
     * This tab-completer will attempt to tab complete:
     * - The player to execute as
     * - The command to execute as (in the context of the player)
     * - The args of the command {@link Command#tabComplete(CommandSender, String, String[])}
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
                                                @NotNull final Command command, @NotNull final String unusedAlias,
                                                @NotNull final String[] args) {
        //System.out.println(sender.isOp());
        if (!sender.hasPermission(permissions.getString("Sudo")) && !sender.isOp()) {
            return Collections.emptyList();
        }
        final Stream<? extends Command> commands =
                Bukkit.getCommandAliases().keySet().stream().map(Bukkit::getPluginCommand)
                        .filter(Objects::nonNull).filter((cmd) -> {
                    if (!sender.isOp()) {
                        if (cmd.getPermission() != null) {
                            return sender.hasPermission(cmd.getPermission());
                        }
                    }
                    return true;
                }); //Get all commands.
        //System.out.println(Bukkit.getCommandAliases().keySet());

        final Iterator<? extends Command> iterator = commands.iterator();
        Collection<String> combined = new HashSet<>();
        while (iterator.hasNext()) {
            //Combine command label and aliases.
            final Command cmd = iterator.next();
            combined.add(cmd.getLabel());
            combined.addAll(cmd.getAliases());
        }
        Stream<String> ret = combined.stream();
        switch (args.length) {
            case 0:
                ret = Bukkit.getOnlinePlayers().stream().map(Player::getName);
                break;
            case 1: //Tab complete player names.
                ret = Bukkit.getOnlinePlayers().stream().map(Player::getName)
                        .filter(name -> name.startsWith(args[0]) || name.equalsIgnoreCase(args[0]));
                break;
            case 2:  //If no args after player decleration, tab complete all commands which the player has perms for.
                //Start filtering commands and aliases.
                final String targetCommand = args[1];
                if (targetCommand.isEmpty()) {
                    break;
                }
                if (targetCommand.startsWith("s:")) {
                    ret = Stream.empty();
                    break;
                }
                //Filter by if the command/alias starts with the target string passed.
                ret = ret.filter(
                        str -> str.startsWith(targetCommand) || str.equalsIgnoreCase(targetCommand));
                break;
            default: //If arg length > 2 then tab based off the known command.
                final Player targetPlayer = Bukkit.getPlayer(args[0]);
                final PluginCommand pluginCommand =
                        Bukkit.getPluginCommand(args[1]); //args[1] is the target command.
                if (pluginCommand == null) {
                    return Collections.emptyList();
                }
                ret = targetPlayer == null ? Stream.empty() : //If player is null --> empty stream.
                        pluginCommand.tabComplete(targetPlayer, unusedAlias,
                                Arrays.copyOfRange(args, 2, args.length - 1)).stream();
        }
        //Returns the alphabetically sorted results.
        return ret.sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}
