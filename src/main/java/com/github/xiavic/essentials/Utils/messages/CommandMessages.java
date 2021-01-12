package com.github.xiavic.essentials.Utils.messages;

import org.jetbrains.annotations.NotNull;

public class CommandMessages extends AbstractMessageProvider {

    public static final CommandMessages INSTANCE = new CommandMessages();
    @NotNull
    public Message messageInventoryCleared = createMessage("inventory-clear",
            "&7Your inventory has just been cleared. Hopefully you didn't have any God-like items on you!");
    @NotNull
    public Message messageInventoryClearedOther =
            createMessage("inventory-clear-other", "%target%'s inventory has been cleared!");
    @NotNull
    public Message messageEnderChestOpened =
            createMessage("enderchest", "Opening your ender chest...");
    @NotNull
    public Message messageEnderChestOpenedOther =
            createMessage("enderchest-other", "Opening %target%'s ender chest...");
    @NotNull
    public Message messagePlayerFed =
            createMessage("feed", "Your hunger seems to have subsided for now...");
    @NotNull
    public Message messageAllPlayersFed =
            createMessage("feed-all", "Everyone has been fed...");
    @NotNull
    public Message messagePlayerFedOther =
            createMessage("feed-other", "%target% has been fed");
    @NotNull
    public Message messagePlayerFly = createMessage("fly", "Flying has been %mode%");
    @NotNull
    public Message messagePlayerFlyOther =
            createMessage("fly-other", "Flying for %target%, has been %mode%");
    @NotNull
    public Message messagePlayerChangeGamemode =
            createMessage("gamemode", "Your gamemode has been updated to %mode%");
    @NotNull
    public Message messagePlayerChangeGamemodeOther =
            createMessage("gamemode-other", "%target%'s gamemode has been updated to %mode%");
    @NotNull
    public Message messagePlayerChangeGodMode =
            createMessage("god", "Your godmode has been %mode%");
    @NotNull
    public Message messagePlayerChangeGodModeOther =
            createMessage("god-other", "Godmode for %player% has been %mode%");
    @NotNull
    public Message messagePlayerHealed = createMessage("heal", "&eYou have been healed.");
    @NotNull
    public Message messageAllPlayersHealed =
            createMessage("heal-all", "You have healed everyone!");
    @NotNull
    public Message messagePlayerHealedOther =
            createMessage("heal-other", "%target% has been healed!");
    @NotNull
    public Message messageRespawnToggle =
            createMessage("respawn-toggle", "You have toggled instant respawn to %mode%");
    @NotNull
    public Message messageSetFirstJoinSpawnPoint =
            createMessage("set-first-join", "&e&lThe First-join Spawn Point has been set");
    @NotNull
    public Message messageSetWorldSpawn =
            createMessage("set-spawn", "&eSpawn has been successfully set.");
    @NotNull
    public Message messageShowWeather =
            createMessage("show-weather", "&eThe server's current weather is, &c%weather%");
    @NotNull
    public Message messageChangeWeather =
            createMessage("show-weather", "&aYou have changed the weather to, &e%weather%");
    @NotNull
    public Message messageDynamicSignEditToggled =
            createMessage("dynamic-sign-edit-toggled", "&aDynamic Sign Editing is now %state%");
    @NotNull
    public Message messageSignEdited =
            createMessage("sign-edited", "&bThe sign has been edited.");
    @NotNull
    public Message messagePlayerVanish = createMessage("vanish", "You are now %mode%");
    @NotNull
    public Message messagePlayerVanishOther =
            createMessage("vanish-other", "%target% is now %mode%");
    @NotNull
    public Message messageWhoisHelp = createMessage("whois-help", "/whois <player>");
    @NotNull
    public Message messageCheatExp =
            createMessage("cheat-exp", "&8[&cDebug&8] &7Your Exp has been set to 9999 levels.");
    @NotNull
    public Message messageCheatArmor =
            createMessage("cheat-armor", "&8[&cDebug&8] &7You have been given the CheatArmor set!");
    @NotNull
    public Message messagePlayerAnnoy = createMessage("annoy", "&4&l&oANNOYANCE");
    @NotNull
    public Message messagePlayerArgh =
            createMessage("argh", "&bArghhh, Piratey treasure awaits at yee spawn!");
    @NotNull
    public Message messageHeadSlotTaken =
            createMessage("head-slot-taken", "&bArghhh, Piratey treasure awaits at yee spawn!");
    @NotNull
    public Message messagePony =
            createMessage("pony", "%PonyPerson% &rsays: %Random_Pony%");
    @NotNull
    public Message messagePonyPerson =
            createMessage("pony-person", "PickleGodLordOmegaLul");
    @NotNull
    public Message messagePlayerChangeWalkSpeed =
            createMessage("walk-speed", "Your walkspeed has been set to, %amount%");
    @NotNull
    public Message messagePlayerChangeFlySpeed =
            createMessage("fly-speed", "Your flyspeed has been set to, %amount%");
    @NotNull
    public Message messagePlayerEquipHat =
            createMessage("hat", "You have equipped %item% to your head area");
    @NotNull
    public Message messagePlayerExtinguishedSelf =
            createMessage("extinguish", "&cYour inner demon has calmed down");
    @NotNull
    public Message messagePlayerExtinguishedOther =
            createMessage("extinguish-target", "You have been extinguished by, %sender%");
    @NotNull
    public Message messageWhoIsPlayer = createMessage("whois-basic", "%nickname% is %username%");

    private CommandMessages() {

    }

    @Override
    @NotNull
    public Message createMessage(final @NotNull String key, final @NotNull String defaultValue) {
        return super.createMessage("commands." + key, defaultValue);
    }
}
