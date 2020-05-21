package com.github.xiavic.essentials.Utils.Misc;

import org.bukkit.entity.Player;

public enum everythingElse {

    INSTANCE;

    public static boolean isFrozen(Player player) {
        return player.getWalkSpeed() == 0;
    }
}
