package com.github.xiavic.essentials.Commands.player.Essential;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.signedit.ISignEditor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SignEditorCommand implements TabExecutor {

    private final ISignEditor iSignEditor = Main.nmsImpl.getSignEditor();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
                             @NotNull final String label, @NotNull String[] args) {
        if (sender.hasPermission(Main.permissions.getString("SignEdit")) || sender.isOp()) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                final Block lookingAt = player.getTargetBlockExact(4);
                if (lookingAt == null || !(lookingAt.getState() instanceof Sign)) {
                    if (args.length > 0) {
                        final String param0 = args[0].toLowerCase();
                        String toggleMessage = Main.messages.getString("DynamicSignEditToggled");
                        final boolean toggle;
                        //Check dynamic sign editing.
                        if (param0.equalsIgnoreCase("on")) {
                            toggle = true;
                            toggleMessage = toggleMessage.replace("%state%", "&benabled.");
                        } else if (param0.equalsIgnoreCase("off")) {
                            toggle = false;
                            toggleMessage = toggleMessage.replace("%state%", "&cdisabled.");
                        } else {
                            Utils.chat(player, Main.messages.getString("NotLookingAtSign"));
                            return true;
                        }
                        iSignEditor.toggleDynamicEditing(player.getUniqueId(), toggle);
                        Utils.chat(player, toggleMessage);
                        return true;
                    } else {
                        final String messageKey =
                                iSignEditor.isDynamicEditingEnabled(player.getUniqueId()) ?
                                        "NotLookingAtSign" :
                                        "SpecifyTarget";

                        Utils.chat(player, Main.messages.getString(messageKey));
                    }
                    return true;
                }
                final Sign sign = (Sign) lookingAt.getState();
                int line;
                if (args.length < 1) {
                    if (iSignEditor.isDynamicEditingEnabled(player.getUniqueId())) {
                        iSignEditor.openUI(player, sign);
                    } else {
                        Utils.chat(player, Main.messages.getString("SpecifyTarget"));
                        return true;
                    }
                }
                String toggleMessage = Main.messages.getString("DynamicSignEditToggled");
                boolean isArgs0Special = false;
                switch (args[0]
                        .toLowerCase()) { //Switch case still necessary because people may want to toggle even when looking at a sign.
                    case "on":
                        iSignEditor.toggleDynamicEditing(player.getUniqueId(), true);
                        toggleMessage = toggleMessage.replace("%state%", "&benabled.");
                        Utils.chat(player, toggleMessage);
                        return true;
                    case "off":
                        iSignEditor.toggleDynamicEditing(player.getUniqueId(), false);
                        toggleMessage = toggleMessage.replace("%state%", "&cdisabled.");
                        Utils.chat(player, toggleMessage);
                        return true;
                    default:
                        try {
                            if (args[0].startsWith("\\") && args[0].length() > 1) {
                                line = 1;
                                args[0] = args[0].substring(1);
                                isArgs0Special = true;
                                break;
                            }
                            line = Integer.parseInt(args[0]);
                        } catch (NumberFormatException ex) {
                            line = 1;
                        }
                }
                if (line < 1 || line > 4) { //If lines are out of bounds.
                    String invalidArgs = Main.messages.getString("InvalidArgs");
                    invalidArgs = invalidArgs
                            .replace("%reason%", "Line number must be a number between 1 and 4!");
                    Utils.chat(player, invalidArgs);
                    return true;
                }
                String[] lines = parseSignInput(Arrays.copyOfRange(args, isArgs0Special ? 0 : 1,
                        args.length)); //Check if first arg was a line-identifier.
                if (lines.length > 5
                        - line) { //5 = MaxLines (4) + (User readbility (1) - line number = Max additional lines.
                    Utils.chat(player, Main.messages.getString("SignTooManyLines")
                            .replace("%lines%", String.valueOf(lines.length)));
                    return true;
                }
                for (String str : lines) { //Check for char overflow.
                    if (str.length() > 15) {
                        Utils.chat(player, Main.messages.getString("SignLineCharOverflow")
                                .replace("%length%", String.valueOf(str.length())));
                        return true;
                    }
                }
                int parsedIndex = 0; //Current index of lines which are parsed.
                for (int index = 0; index < 4; index++) {
                    if (index < line - 1) {
                        sign.setLine(index, "");
                        continue;
                    }
                    sign.setLine(index, parsedIndex < lines.length ? lines[parsedIndex++] : "");
                }
                Utils.chat(player,
                        Main.messages.getString(sign.update(true) ? "SignEdited" : "SignEditFailed"));
            } else {
                Utils.chat(Main.messages.getString("SenderNotPlayer"));
                return true;
            }
        } else {
            Utils.chat(Main.messages.getString("NoPerms"));
        }
        return true;
    }

    /**
     * Parse split-up inputs in the sign into lines.
     *
     * @param args The arguments.
     * @return Returns a formatted String[] representing each line of the sign.
     */
    public String[] parseSignInput(String[] args) {
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
        return lines.toArray(new String[0]);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
                                                @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final String onOff =
                    iSignEditor.isDynamicEditingEnabled(player.getUniqueId()) ? "off" : "on";
            final Block lookingAt = player.getTargetBlockExact(4);
            if (args.length == 1) {
                if (lookingAt == null || !(lookingAt.getState() instanceof Sign)) {
                    return Collections.singletonList(onOff);
                }
                return Arrays.asList("1", "2", "3", "4", onOff);
            }
        }
        return Collections.emptyList();
    }
}
