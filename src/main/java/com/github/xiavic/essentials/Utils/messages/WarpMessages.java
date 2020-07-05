package com.github.xiavic.essentials.Utils.messages;

import org.jetbrains.annotations.NotNull;

public class WarpMessages extends AbstractMessageProvider {

    public static final WarpMessages INSTANCE = new WarpMessages();
    @NotNull public Message messageWarpCreated =
        createMessage("created", "Warp \"%name%\" has been created.");
    @NotNull public Message messageWarpDeleted = createMessage("deleted", "Warp was deleted.");
    @NotNull public Message messageWarpToggled =
        createMessage("toggled", "This warp is now %enabled%");
    @NotNull public Message messageWarpTeleportSuccessOther =
        createMessage("teleport-success-other", "%player% was successfully teleported to %warp%");
    @NotNull public Message messageWarpFailureOther =
        createMessage("other-failure", "%player% could not be warped, reason: %reason%");
    @NotNull public Message messageWarpShowPermission =
        createMessage("permission", "The permission for this warp is: %permission%");
    @NotNull public Message messageWarpPermissionEdited =
        createMessage("permission-edited", "The permission for this warp has been updated");
    @NotNull public Message messageWarpNullPermission =
        createMessage("null-permission", "This warp has no permission set");
    @NotNull public Message messageWarpWhitelistToggled =
        createMessage("whitelist", "Warp whitelist has been %enabled%.");
    @NotNull public Message messageWarpBlacklistToggled =
        createMessage("blacklist", "Warp blacklist has been %enabled%.");
    @NotNull public Message messageWarpAccessDenied =
        createMessage("access-denied", "You may not access this warp!");
    @NotNull public Message messageWarpFailureDisabled =
        createMessage("failure-disabled", "This warp is disabled.");
    @NotNull public Message messageWarpPlayerWhitelistToggled =
        createMessage("whitelist-player", "%player% has been %operation% to the whitelist.");
    @NotNull public Message messageWarpPlayerBlacklistToggled =
        createMessage("whitelist-player", "%player% has been %operation% to the blacklist.");
    @NotNull public Message messageWarpUnsafe = createMessage("unsafe", "That warp is not safe!");
    @NotNull public Message messageWarpListHeader = createMessage("list-header", "Warps:");
    @NotNull public Message messageWarpListElementHeader =
        createMessage("list-element-header", "  %warp%");
    @NotNull public Message messageWarpListElementInfo = createMessage("list-element-info",
        "     Name: %name%, Permission: %permission%, Location: %location%, Enabled: %enabled%");
    @NotNull public Message messagePrivateWarpListHeader =
        createMessage("private-list-header", "Private Warps: ");
    @NotNull public Message messagePrivateWarpListElementHeader =
        createMessage("private-list-element-header", "  %warp%");
    @NotNull public Message messagePrivateWarpListElementInfo =
        createMessage("private-list-element-info",
            "     Name: %name%, Owner: %owner%, Location: %location%, Enabled: %enabled%");

    private WarpMessages() {

    }

    @Override public @NotNull Message createMessage(final @NotNull String key,
        final @NotNull String defaultValue) {
        return super.createMessage("warp." + key, defaultValue);
    }
}
