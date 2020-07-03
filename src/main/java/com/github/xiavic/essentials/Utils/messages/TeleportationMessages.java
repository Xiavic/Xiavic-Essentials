package com.github.xiavic.essentials.Utils.messages;

import org.jetbrains.annotations.NotNull;

public class TeleportationMessages extends AbstractMessageProvider {

    public static final TeleportationMessages INSTANCE = new TeleportationMessages();

    @NotNull public Message messageTeleported =
        createMessage("teleported", "%target1% has been teleported to %target2%");
    @NotNull public Message messageTeleportedSpawn =
        createMessage("teleported-spawn", "You have been successfully teleported to spawn.");
    @NotNull public Message messageTeleportationDisabled =
        createMessage("teleport-disabled", "%target% has teleportation disabled!");
    @NotNull public Message messageTeleportRequestSent =
        createMessage("teleport-request-sent", "You have sent a teleport request to %target%");
    @NotNull public Message messageTeleportRequestReceived =
        createMessage("teleport-requested", "%sender% has requested to teleport to you");
    @NotNull public Message messageTeleportRequestAccepted =
        createMessage("teleport-request-acccepted",
            "%target% has accepted your teleport request. You will be teleported in %time% seconds.");
    @NotNull public Message messageTeleportAccepted =
        createMessage("teleport-accepted", "You have accepted %sender%'s request");
    @NotNull public Message messageTeleportRequestDenied = createMessage("teleport-request-denied",
        "%target% has denied your teleport request. You fucking suck.");
    @NotNull public Message messageTeleportDenied =
        createMessage("teleport-denied", "You have denied %sender%'s request");
    @NotNull public Message messageTeleportOnCooldown =
        createMessage("teleport-cooldown", "You must wait %time% seconds until you can tpa again.");
    @NotNull public Message messageTeleportPending =
        createMessage("teleport-pending", "You already have a request pending. Pendejo");
    @NotNull public Message messageTeleportToggleDisabled = createMessage("teleport-toggled-off",
        "You have DISABLED teleport requests to you. Pathetic...");
    @NotNull public Message messageTeleportToggleEnabled =
        createMessage("teleport-toggled-on", "You have ENABLED teleport requests to you.");
    @NotNull public Message messageTeleportedRandomly = createMessage("random-teleport",
        "You have been randomly teleported. Don't have too much fun!");
    @NotNull public Message messageTeleportFailed =
        createMessage("failure", "Teleportation has failed you once again!");
    @NotNull public Message messageTeleportSelf =
        createMessage("teleport-self", "You cannot teleport to yourself, idiot.");
    @NotNull public Message messageTeleportingLastLocation =
        createMessage("teleporting-previous", "Returning to your last location");
    @NotNull public Message messageNoPreviousLocation =
        createMessage("no-previous-location", "You do not have a recent location to go back to");

    private TeleportationMessages() {
    }

    @Override public @NotNull Message createMessage(final @NotNull String key,
        final @NotNull String defaultValue) {
        return super.createMessage("telport." + key, defaultValue);
    }
}
