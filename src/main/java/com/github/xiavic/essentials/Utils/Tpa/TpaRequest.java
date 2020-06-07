package com.github.xiavic.essentials.Utils.Tpa;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaRequest {

    // private FileConfiguration m = Messages.get();

    private final Player origin;
    private final Player target;
    private final long requestTime;

    public TpaRequest(@NotNull final Player origin, @NotNull final Player target) {
        this.origin = origin;
        this.target = target;
        this.requestTime = System.currentTimeMillis();
        sendRequest();
    }

    public void sendRequest() {
        Utils.chat(this.origin, Main.messages.getString("Tpa").replace("%target%", target.getDisplayName()));
        Utils.chat(this.target, Main.messages.getString("Tpa_Request").replace("%sender%", origin.getDisplayName()));
    }

    @NotNull
    public Player getOrigin() {
        return this.origin;
    }

    @NotNull
    public Player getTarget() {
        return this.target;
    }

    public boolean isDead(int duration) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - requestTime) / 1000 > duration;
    }

}
