package com.github.xiavic.essentials.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandBooleanValue {

    public static final List<String> TRUE_VALUES = Arrays.asList("true", "enabled", "on");
    public static final List<String> FALSE_VALUES = Arrays.asList("false", "disabled", "off");
    public static final List<String> ALL_VALUES;
    public static final CommandBooleanValue TRUE = new CommandBooleanValue(true);
    public static final CommandBooleanValue FALSE = new CommandBooleanValue(false);

    static {
        ALL_VALUES = new ArrayList<>(TRUE_VALUES.size() + FALSE_VALUES.size());
        ALL_VALUES.addAll(TRUE_VALUES);
        ALL_VALUES.addAll(FALSE_VALUES);
    }

    public final boolean value;

    private CommandBooleanValue(final boolean value) {
        this.value = value;
    }

    public static Optional<CommandBooleanValue> fromString(final String value) {
        if (value == null) {
            return Optional.empty();
        }
        for (String s : TRUE_VALUES) {
            if (s.equalsIgnoreCase(value)) {
                return Optional.of(TRUE);
            }
        }
        for (String s : FALSE_VALUES) {
            if (s.equalsIgnoreCase(value)) {
                return Optional.of(FALSE);
            }
        }
        return Optional.empty();
    }

    public static CommandBooleanValue fromBoolean(final boolean value) {
        return value ? TRUE : FALSE;
    }

}
