package com.github.xiavic.essentials.Commands.messages;

import com.github.xiavic.essentials.Utils.messages.AbstractMessageProvider;
import com.github.xiavic.essentials.Utils.messages.Message;
import org.jetbrains.annotations.NotNull;

public class TeleportationMessages extends AbstractMessageProvider {

    public static final TeleportationMessages INSTANCE = new TeleportationMessages();

    @NotNull public Message messageTeleported =
        createMessage("TpRemote", "%target1% has been teleported to %target2%");
    @NotNull public Message messageTeleportationDisabled =
        createMessage("TpDisabled", "%target% has teleportation disabled!");
    @NotNull public Message messageTeleportRequestSent =
        createMessage("Tpa", "You have sent a teleport request to %target%");
    @NotNull public Message messageTeleportRequestReceived =
        createMessage("Tpa_Recieved", "%sender% has requested to teleport to you");
    @NotNull public Message messageTeleportRequestAccepted = createMessage("Tpa_Accept",
        "%target% has accepted your teleport request. You will be teleported in %time% seconds.");
    @NotNull public Message messageTeleportAccepted =
        createMessage("Tpa_Accept1", "You have accepted %sender%'s request");
    @NotNull public Message messageTeleportRequestDenied =
        createMessage("Tpa_Deny", "%target% has denied your teleport request. You fucking suck.");
    @NotNull public Message messageTeleportDenied =
        createMessage("Tpa_Deny1", "You have denied %sender%'s request");
    @NotNull public Message messageTeleportOnCooldown =
        createMessage("TpaCooldown", "You must wait %time% seconds until you can tpa again.");
    @NotNull public Message messageTeleportPending =
        createMessage("TpaPending", "You already have a request pending. Pendejo");
    @NotNull public Message messageTeleportToggleDisabled =
        createMessage("TpToggleOff", "You have DISABLED teleport requests to you. Pathetic...");
    @NotNull public Message messageTeleportToggleEnabled =
        createMessage("TpToggleOn", "You have ENABLED teleport requests to you.");
    @NotNull public Message messageTeleportedRandomly =
        createMessage("RTP", "You have been randomly teleported. Don't have too much fun!");
    @NotNull public Message messageTeleportFailed =
        createMessage("Tp_Failed", "Teleportation has failed you once again!");
    @NotNull public Message messageTeleportSelf =
        createMessage("TpSelf", "You cannot teleport to yourself, idiot.");
    @NotNull public Message messageTeleportingLastLocation =
        createMessage("Back", "Returning to your last location");
    @NotNull public Message messageNoPreviousLocation =
        createMessage("BackNone", "You do not have a recent location to go back to");

    private TeleportationMessages() {
    }
}
