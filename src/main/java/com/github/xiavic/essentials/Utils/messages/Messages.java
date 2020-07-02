package com.github.xiavic.essentials.Utils.messages;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the default messages which can be utilized across most if not all commands.
 */
public final class Messages extends AbstractMessageProvider {

    public static final Messages INSTANCE = new Messages();

    @NotNull public Message messageSenderNotPlayer =
        createMessage("SenderNotPlayer", "&cYou are not a fucking player!");

    @NotNull public Message messageNoPlayerFound =
        createMessage("PlayerNotFound", "&cThat Player is not found or is not online!");

    @NotNull public Message messageVarArgPlayerNotFound =
        createMessage("PlayerNotFound2", "&cOne of the players are not online!");

    @NotNull public Message messageSpecifyTarget =
        createMessage("SpecifyTarget", "&cYou must specify the target");

    @NotNull public Message messageUnsupportedServerVersion =
        createMessage("ServerVersionUnsupported",
            "&cSorry, your server version, %version%, is unsupported.");

    @NotNull public Message messageNoSuchWorld = createMessage("NoSuchWorld", "&cUnknown world");

    @NotNull public Message messageNoSuchCommand =
        createMessage("NoSuchCommand", "&cUnknownCommand");

    @NotNull public Message messageNoPermission =
        createMessage("NoPerms", "&4You have no power here...");

    @NotNull public Message messageEnabled = createMessage("Enabled", "Enabled... bitch");

    @NotNull public Message messageDisabled = createMessage("Disabled", "Disabled... bitch");

    @NotNull public Message messageValueTrue = createMessage("True", "Skeet skeet mudafuka");

    @NotNull public Message messageValueFalse = createMessage("False", "False... usu");

    @NotNull public Message messagePrefix = createMessage("Prefix", "[Xiavic]");

}
