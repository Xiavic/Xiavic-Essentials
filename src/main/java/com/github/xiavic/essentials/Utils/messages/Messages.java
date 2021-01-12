package com.github.xiavic.essentials.Utils.messages;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the default messages which can be utilized across most if not all commands.
 */
public final class Messages extends AbstractMessageProvider {

    public static final Messages INSTANCE = new Messages();

    @NotNull
    public Message messageSenderNotPlayer =
            createMessage("sender-not-player", "&cYou are not a fucking player!");

    @NotNull
    public Message messageNoPlayerFound =
            createMessage(".unknown-player", "&cThat Player is not found or is not online!");

    @NotNull
    public Message messageVarArgPlayerNotFound =
            createMessage("vararg-unknown-player", "&cOne of the players are not online!");

    @NotNull
    public Message messageSpecifyTarget =
            createMessage("specify-target", "&cYou must specify the target");

    @NotNull
    public Message messageUnsupportedServerVersion = createMessage("unsupported-server",
            "&cSorry, your server version, %version%, is unsupported.");

    @NotNull
    public Message messageNoSuchWorld = createMessage("unknown-world", "&cUnknown world");

    @NotNull
    public Message messageNoSuchCommand =
            createMessage("unknown-command", "&cUnknownCommand");

    @NotNull
    public Message messageNoPermission =
            createMessage("no-permission", "&4You have no power here...");

    @NotNull
    public Message messageEnabled = createMessage("plugin-enabled", "Enabled... bitch");

    @NotNull
    public Message messageDisabled = createMessage("plugin-disabled", "Disabled... bitch");

    @NotNull
    public Message messageValueTrue = createMessage("value-true", "Skeet skeet mudafuka");

    @NotNull
    public Message messageValueFalse = createMessage("value-false", "False... usu");

    @NotNull
    public Message messagePrefix = createMessage("prefix", "[Xiavic]");

    @NotNull
    public Message messagePlayerQuit = createMessage("quit", "&8[&c-&8] &c%player%");

    @NotNull
    public Message messagePlayerRejoin = createMessage("rejoin", "&8[&6+&8] &e%player%");

    @NotNull
    public Message messageItemIsAir =
            createMessage("item-is-air", "&cYou are holding air, not an item...");

    @NotNull
    public Message messageInvalidNumber =
            createMessage("invalid-number", "Only whole numbers between 1-10 may be chosen.");

    @NotNull
    public Message messagePlayerFrozen = createMessage("player-frozen",
            "&c&lYou are still frozen, please contact a server administrator to be un-frozen!");

    @NotNull
    public Message messageInventoryFull =
            createMessage("inventory-full", "&cYou have a full inventory. Please free up some space!");

    @NotNull
    public Message messageFirstJoin = createMessage("first-join",
            "&eWelcome to the development server, &e&l%player%, &aonly those with authorized access may use this server.!");

    @NotNull
    public Message messageWhatIsThis =
            createMessage("what-is-this", "What a terrible night for a curse...");

    @NotNull
    public Message messageWhatIsThisTarget =
            createMessage("what-is-this-target", "You have cursed %target%.");

    @NotNull
    public Message messageSignTooManyLines =
            createMessage("sign-too-many-lines", "&eYou have provided too many lines: %lines%");

    @NotNull
    public Message messageSignTooManyCharacters = createMessage("sign-too-many-characters",
            "&eToo many characters Max = 15, Provided: %length%");

    @NotNull
    public Message messageSignEditFailure =
            createMessage("sign-edit-failure", "&eFailed to edit this sign!");

    @NotNull
    public Message messageLookAtSign =
            createMessage("look-at-sign", "&cYou must be looking at a sign!");

    @NotNull
    public Message messageItemRepaired =
            createMessage("item-repaired", "&aItem Repaired!");

    @NotNull
    public Message messageItemRepairFailure =
            createMessage("item-repair-failure", "&cThis item cannot be repaired.");

    @NotNull
    public Message messagePlayerAfk = createMessage("player-afk", "%player% is now AFK");

    @NotNull
    public Message messagePlayerNotAfk =
            createMessage("player-not-afk", "%player% is no longer AFK");

    @NotNull
    public Message messagePlayerVanishEnabled =
            createMessage("vanish-enabled", "Vanished");

    @NotNull
    public Message messagePlayerVanishDisabled =
            createMessage("vanish-disbaled", "Visible");

    @NotNull
    public Message messageConfigUpdated =
            createMessage("config-updated", "&8[&cDebug&8] &7The config files have been updated!");

    @NotNull
    public Message messageShowPluginVersion = createMessage("config-version",
            "&8[&cDebug&8] &7The current running version is %version%...");

    @NotNull
    public Message messageDatabaseError = createMessage("database-error",
            "Connection was NOT set-up properly. Please double check your database settings. :(");

    @NotNull
    public Message messageHeads = createMessage("heads", "Heads");

    @NotNull
    public Message messageTails = createMessage("tails", "Tails");

    @Override
    public @NotNull Message createMessage(final @NotNull String key,
                                          final @NotNull String defaultValue) {
        return super.createMessage("messages." + key, defaultValue);
    }
}
