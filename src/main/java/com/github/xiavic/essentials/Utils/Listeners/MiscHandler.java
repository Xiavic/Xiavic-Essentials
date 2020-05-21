package com.github.xiavic.essentials.Utils.Listeners;

import com.github.xiavic.lib.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class MiscHandler implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChange(final SignChangeEvent event) {
        String[] arr = event.getLines();
        arr = Utils.colorize(arr);
        int index = 0;
        for (final String s : arr) {
            event.setLine(index++, s);
        }
    }

}
