package com.github.xiavic.essentials;

import com.github.xiavic.essentials.Commands.StaffCmds.cheats.CheatArmor;
import com.github.xiavic.essentials.Commands.StaffCmds.cheats.CheatEXP;
import com.github.xiavic.essentials.Commands.StaffCmds.noncheat.*;
import com.github.xiavic.essentials.Commands.StaffCmds.noncheat.teleport.TPPosCommand;
import com.github.xiavic.essentials.Commands.StaffCmds.noncheat.teleport.TPhereCommand;
import com.github.xiavic.essentials.Commands.StaffCmds.noncheat.teleport.TeleportCommand;
import com.github.xiavic.essentials.Commands.StaffCmds.noncheat.teleport.TpallCommand;
import com.github.xiavic.essentials.Commands.UserCmds.Essential.*;
import com.github.xiavic.essentials.Commands.UserCmds.Essential.Teleport.BackCommand;
import com.github.xiavic.essentials.Commands.UserCmds.Essential.Teleport.RandomTPCommand;
import com.github.xiavic.essentials.Commands.UserCmds.Essential.Teleport.SpawnCommand;
import com.github.xiavic.essentials.Commands.UserCmds.Essential.Teleport.Tpa.TpaCommand;
import com.github.xiavic.essentials.Commands.UserCmds.Essential.Teleport.Tpa.TpacceptCommand;
import com.github.xiavic.essentials.Commands.UserCmds.Essential.Teleport.Tpa.TpdenyCommand;
import com.github.xiavic.essentials.Commands.UserCmds.Fun.*;
import com.github.xiavic.essentials.Commands.UserCmds.Fun.Links.*;
import com.github.xiavic.essentials.Utils.EquipAnything.EquipEvents;
import com.github.xiavic.essentials.Utils.Listeners.AFKHandler;
import com.github.xiavic.essentials.Utils.Listeners.JoinQuit;
import com.github.xiavic.essentials.Utils.Listeners.RespawnEvent;
import com.github.xiavic.essentials.Utils.Listeners.TeleportHandler;
import com.github.xiavic.essentials.Utils.Tpa.TpaHandler;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.lib.NMSHandler.NMS;
import com.github.xiavic.lib.inventory.InventorySerializer;
import com.github.xiavic.lib.signedit.ISignEditor;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import com.github.xiavic.lib.teleport.ITeleportRequestHandler;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;


public final class Main extends JavaPlugin {

    public static Yaml permissions;
    public static Yaml messages;
    public static Yaml mainConfig;
    public static Yaml commands;
    public static TpaHandler tpaHandler;
    public static TeleportHandler teleportHandler;
    public static NMS nmsImpl; //Should never be null after plugin init has completed.
    private static Main instance;

    // Handle Instance of plugin in multiple classes.
    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        if (!registerNMSHandler()) { //If NMS compat failed, exit.
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        setupStorage(); // Loads or Copy Default Configuration
        // loadshit();

        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(" XIAVIC ESSENTIALS IS ACTIVATED... ");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("Xiavic Network's Amazing Essentials");
        Bukkit.getConsoleSender().sendMessage("     In Development by the Xiavic Dev Team ");
        Bukkit.getConsoleSender().sendMessage(" ");
        registerShit();
        registerListeners();
        registerCommands();
        Bukkit.getScheduler().runTaskTimer(this, tpaHandler::doChecks, 0, 20);
    }

    public void onDisable() {

    }

    // TODO: Check if Object is not Null so we can catch exceptions!
    private void registerCommands() {
        //getCommand("fireball").setExecutor(new FireBallCommand());
        getCommand("argh").setExecutor(new ArghCommand());
        getCommand("afk").setExecutor(new AFKCommand());
        getCommand("back").setExecutor(new BackCommand());
        getCommand("cartography").setExecutor(new CartographyCommand());
        getCommand("cheatarmor").setExecutor(new CheatArmor());
        getCommand("cheatexp").setExecutor(new CheatEXP());
        getCommand("clear").setExecutor(new ClearCommand());
        getCommand("clearall").setExecutor(new ClearAllCommand());
        //getCommand("coinflip").setExecutor(new CoinFlipCommand());
        getCommand("coreconfigupdate").setExecutor(new ConfigReloadCommand());
        getCommand("coreversion").setExecutor(new CoreVersionCommand());
        getCommand("discord").setExecutor(new DiscordCommand());
        getCommand("dispose").setExecutor(new DisposeCommand());
        getCommand("ec").setExecutor(new EnderChestCommand());
        getCommand("ext").setExecutor(new ExtinguishCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("flyspeed").setExecutor(new FlySpeedCommand());
        getCommand("forums").setExecutor(new ForumsCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("god").setExecutor(new GodCommand());
        getCommand("grindstone").setExecutor(new GrindstoneCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("head").setExecutor(new HeadCommand());
        getCommand("info").setExecutor(new ItemInfoCommand());
        getCommand("loom").setExecutor(new LoomCommand());
        getCommand("more").setExecutor(new MoreCommand());
        getCommand("near").setExecutor(new NearCommand());
        getCommand("pony").setExecutor(new PonyCommand());
        getCommand("privatewarp").setExecutor(new PrivateWarpCommand());
        getCommand("rtp").setExecutor(new RandomTPCommand());
        getCommand("repair").setExecutor(new RepairCommand());
        getCommand("setfirstspawn").setExecutor(new FirstSpawnSetCommand());
        getCommand("setspawn").setExecutor(new SpawnSetCommand());
        getCommand("signedit").setExecutor(new SignEditorCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("stonecutter").setExecutor(new StonecutterCommand());
        getCommand("sudo").setExecutor(new SudoCommand());
        getCommand("suicide").setExecutor(new SuicideCommand());
        getCommand("top").setExecutor(new TopCommand());
        getCommand("tp").setExecutor(new TeleportCommand());
        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("tpaccept").setExecutor(new TpacceptCommand());
        getCommand("tpall").setExecutor(new TpallCommand());
        getCommand("tpdeny").setExecutor(new TpdenyCommand());
        getCommand("tphere").setExecutor(new TPhereCommand());
        getCommand("tppos").setExecutor(new TPPosCommand());
        getCommand("twitter").setExecutor(new TwitterCommand());
        getCommand("walkspeed").setExecutor(new WalkSpeedCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("warpedit").setExecutor(new WarpEditCommand());
        getCommand("website").setExecutor(new WebsiteCommand());
        getCommand("whois").setExecutor(new WhoIsCommand());
        getCommand("world").setExecutor(new WorldCommand());
        getCommand("workbench").setExecutor(new WorkbenchCommand());
        getCommand("youtube").setExecutor(new YoutubeCommand());
        getCommand("hat").setExecutor(new HatCommand());
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinQuit(), this);
        pm.registerEvents(new EquipEvents(), this);
        pm.registerEvents(new RespawnEvent(), this);
        pm.registerEvents(teleportHandler, this);
        AFKHandler.INSTANCE.registerTicker();
        pm.registerEvents(nmsImpl.getSignEditor(), this);
    }

    // Use this function for creating new shit
    private void registerShit() {
        tpaHandler = new TpaHandler();
        teleportHandler = new TeleportHandler();
    }

    private boolean registerNMSHandler() {
        if (Main.nmsImpl == null) {
            try {
                final Class<?> clazz = Class
                    .forName("com.github.xiavic.essentials" + Utils.parseNMSVersion() + ".NMSImpl");
                final Class<? extends NMS> nmsImplClass = clazz.asSubclass(NMS.class);
                Main.nmsImpl = nmsImplClass.getDeclaredConstructor().newInstance();
            } catch (final ReflectiveOperationException ex) {
                ex.printStackTrace();
                final String message = messages.getString("ServerVersionUnsupported");
                getLogger().log(Level.SEVERE,
                    Utils.chat(message.replace("%version%", Bukkit.getVersion())));
                return false;
            }
        }
        return true;
    }

    private void registerProviders() {
        final ServicesManager serviceManager = Bukkit.getServicesManager();
        //Register NMS implementations
        serviceManager
            .register(ISignEditor.class, nmsImpl.getSignEditor(), this, ServicePriority.Low);
        serviceManager.register(InventorySerializer.class, nmsImpl.getInventorySerializer(), this,
            ServicePriority.Low);
        //Register Teleport handlers
        serviceManager
            .register(ITeleportHandler.class, new TeleportHandler(), this, ServicePriority.Low);
        serviceManager
            .register(ITeleportRequestHandler.class, new TpaHandler(), this, ServicePriority.Low);
        //
    }

    // Handling of configuration file with Json, Yaml, and Toml
    private void setupStorage() {

        ////////////////
        // permissions.yml
        ////////////////
        permissions =
            LightningBuilder.fromFile(new File("plugins/XiavicCore/Resources/permissions"))
                .addInputStreamFromResource("permissions.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY).setDataType(DataType.SORTED)
                .createConfig();

        ////////////////
        // messages.yml
        ////////////////
        messages = LightningBuilder.fromFile(new File("plugins/XiavicCore/Resources/messages"))
            .addInputStreamFromResource("messages.yml")
            .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
            .setReloadSettings(ReloadSettings.AUTOMATICALLY).setDataType(DataType.SORTED)
            .createConfig();

        ////////////////
        // config.yml
        ////////////////
        mainConfig = LightningBuilder.fromFile(new File("plugins/XiavicCore/config"))
            .addInputStreamFromResource("config.yml")
            .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
            .setReloadSettings(ReloadSettings.AUTOMATICALLY).setDataType(DataType.SORTED)
            .createConfig();

        //     ////////////////
        //     // commands.yml
        //     ////////////////
        //     commands = LightningBuilder
        //             .fromFile(new File("plugins/XiavicCore/Resources/commands"))
        //             .addInputStreamFromResource("commands.yml")
        //             .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
        //             .setReloadSettings(ReloadSettings.AUTOMATICALLY)
        ///             .setDataType(DataType.SORTED)
        //             .createConfig();

        //     ////////////////
        //     // players.json
        //     ////////////////
        //     Json players = new Json("players", Bukkit.getServer().getWorldContainer() + "/plugins/XiavicCore/Resources");
        //     players.set("players." + "NightPotato.Rank", "ThatNewGuy!");


    }

    private void loadshit() {
        saveResource("Resources/permissions.yml", false);
        saveResource("Resources/messages.yml", false);
        //saveResource("Resources/commands.yml", false);
        saveResource("config.yml", false);
    }

    // I am using this function for updating the configs from the files inside the current
    // build of the plugin and preserves the spawn location in the mainConfig
    public void updateShit() {
        String firstspawnLocation = mainConfig.getString("FirstSpawn");
        String spawnLocation = mainConfig.getString("Spawn");
        saveResource("Resources/permissions.yml", true);
        saveResource("Resources/messages.yml", true);
        //saveResource("Resources/commands.yml", true);
        saveResource("config.yml", true);
        mainConfig.set("FirstSpawn", firstspawnLocation);
        mainConfig.set("Spawn", spawnLocation);
        saveConfig();
    }

}
