package com.github.xiavic.essentials.Utils.Tpa;

import com.github.xiavic.essentials.Main;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import com.github.xiavic.lib.teleport.ITeleportRequestHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TpaHandler implements ITeleportRequestHandler {

    // private FileConfiguration m = Messages.get();

    private ITeleportHandler teleportHandler;
    private List<TpaRequest> requests = new ArrayList<>();
    private Map<TpaRequest, Long> teleports = new HashMap<>();
    private Map<Player, Long> cooldowns = new HashMap<>();
    private final int requestTimeout;
    private final int teleportTime;
    private final int tpaCooldown;
    private List<TpaRequest> deadTeleports = new ArrayList<>();
    private List<TpaRequest> deadRequests = new ArrayList<>();
    private List<Player> deadCooldowns = new ArrayList<>();

    /**
     * This method should be called after startup - wait until other plugins have a chance to register their handlers into lib.
     */
    @Override public boolean loadTeleportHandler() {
        RegisteredServiceProvider<ITeleportHandler> rsp =
            Bukkit.getServicesManager().getRegistration(ITeleportHandler.class);
        if (rsp != null) {
            teleportHandler = rsp.getProvider();
        }
        return rsp != null;
    }

    public TpaHandler() {
        requestTimeout = Main.mainConfig.getInt("TpaTimeout");
        teleportTime = Main.mainConfig.getInt("TpaDelay");
        tpaCooldown = Main.mainConfig.getInt("TpaCooldown");
    }

    @Override public void startCooldown(@NotNull final Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    @Override public boolean canTpa(@NotNull final Player player) {
        if (cooldowns.containsKey(player)) {
            int remaining =
                (int) (tpaCooldown - ((System.currentTimeMillis() - cooldowns.get(player)) / 1000));
            player.sendMessage(Utils.chat(Main.messages.getString("TpaCooldown")
                .replace("%time%", String.valueOf(remaining))));
            return false;
        }
        return true;
    }

    @Override public void parseRequest(@NotNull final Player player, final boolean accepted) {
        for (final TpaRequest request : requests) {
            if (request.getTarget() == player) {
                if (accepted) {
                    request.getOrigin().sendMessage(Utils.chat(Main.messages.getString("Tpa_Accept")
                        .replace("%target%", request.getTarget().getDisplayName())
                        .replace("%time%", String.valueOf(teleportTime))));
                    request.getTarget().sendMessage(Utils.chat(
                        Main.messages.getString("Tpa_Accept1")
                            .replace("%sender%", request.getOrigin().getDisplayName())));
                    teleports.put(request, System.currentTimeMillis());
                } else {
                    request.getOrigin().sendMessage(Utils.chat(Main.messages.getString("Tpa_Deny")
                        .replace("%target%", request.getTarget().getDisplayName())
                        .replace("%time%", String.valueOf(teleportTime))));
                    request.getTarget().sendMessage(Utils.chat(Main.messages.getString("Tpa_Deny1")
                        .replace("%sender%", request.getOrigin().getDisplayName())));
                }
                requests.remove(request);
                return;
            }
        }
        player.sendMessage(Utils.chat(Main.messages.getString("NoRequest")));
    }

    // 0 - success
    // 1 - tpa already pending
    // 2 - tpa disabled
    @Override public int addRequest(Player origin, Player target) {
        for (TpaRequest tpr : requests) {
            if (tpr.getOrigin() == origin) {
                origin.sendMessage(Utils.chat(Main.messages.getString("TpaPending")));
                return 1;
            }
        }
        if (teleportHandler.isDisabled(target))
            return 2;
        requests.add(new TpaRequest(origin, target));
        startCooldown(origin);
        return 0;
    }

    private void checkRequests() {
        for (TpaRequest request : requests) {
            if (request.isDead(requestTimeout)) {
                System.out.println("Teleport request timed out");
                deadRequests.add(request);
            }
        }
        requests.removeAll(deadRequests);
        deadRequests.clear();
    }

    private void checkTeleports() {
        for (Map.Entry<TpaRequest, Long> teleport : teleports.entrySet()) {
            if ((System.currentTimeMillis() - teleport.getValue()) / 1000 > teleportTime) {
                System.out.println("Teleport request fulfilled");
                TpaRequest request = teleport.getKey();
                teleportHandler.teleport(request.getOrigin(), request.getTarget(), false);
                deadTeleports.add(request);
            }
        }
        for (TpaRequest tpr : deadTeleports) {
            teleports.remove(tpr);
            requests.remove(tpr);
        }
    }

    private void checkCooldowns() {
        for (Map.Entry<Player, Long> cooldown : cooldowns.entrySet()) {
            if ((System.currentTimeMillis() - cooldown.getValue()) / 1000 > tpaCooldown) {
                System.out.println("Cooldown Expired");
                deadCooldowns.add(cooldown.getKey());
            }
        }
        for (Player player : deadCooldowns) {
            cooldowns.remove(player);
        }
    }


    @Override public void doChecks() {
        checkRequests();
        checkTeleports();
        checkCooldowns();
    }

}
