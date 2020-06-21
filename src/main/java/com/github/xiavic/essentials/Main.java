package com.github.xiavic.essentials;

// import com.github.xiavic.essentials.Utils.Misc.Databases;

import com.github.xiavic.essentials.Commands.player.Essential.*;
import com.github.xiavic.essentials.Commands.player.Essential.Teleport.BackCommand;
import com.github.xiavic.essentials.Commands.player.Essential.Teleport.RandomTPCommand;
import com.github.xiavic.essentials.Commands.player.Essential.Teleport.SpawnCommand;
import com.github.xiavic.essentials.Commands.player.Essential.Teleport.Tpa.TpaCommand;
import com.github.xiavic.essentials.Commands.player.Essential.Teleport.Tpa.TpacceptCommand;
import com.github.xiavic.essentials.Commands.player.Essential.Teleport.Tpa.TpdenyCommand;
import com.github.xiavic.essentials.Commands.player.Fun.*;
import com.github.xiavic.essentials.Commands.player.Fun.Links.*;
import com.github.xiavic.essentials.Commands.staff.cheats.CheatArmor;
import com.github.xiavic.essentials.Commands.staff.cheats.CheatEXP;
import com.github.xiavic.essentials.Commands.staff.noncheat.*;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TPPosCommand;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TPhereCommand;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TeleportCommand;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TpallCommand;
import com.github.xiavic.essentials.Utils.EquipAnything.EquipEvents;
import com.github.xiavic.essentials.Utils.Listeners.*;
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
    public static Yaml database;
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
        getCommand(Main.commands.getString("Argh")).setExecutor(new ArghCommand());
        getCommand(Main.commands.getString("Afk")).setExecutor(new AFKCommand());
        getCommand(Main.commands.getString("Back")).setExecutor(new BackCommand());
        getCommand(Main.commands.getString("Cartography")).setExecutor(new CartographyCommand());
        getCommand(Main.commands.getString("CheatArmor")).setExecutor(new CheatArmor());
        getCommand(Main.commands.getString("CheatEXP")).setExecutor(new CheatEXP());
        getCommand(Main.commands.getString("Clear")).setExecutor(new ClearCommand());
        getCommand(Main.commands.getString("ClearAll")).setExecutor(new ClearAllCommand());
        //getCommand("coinflip").setExecutor(new CoinFlipCommand());
        getCommand(Main.commands.getString("Coreconfigupdate")).setExecutor(new ConfigReloadCommand());
        getCommand(Main.commands.getString("Coreversion")).setExecutor(new CoreVersionCommand());
        getCommand(Main.commands.getString("Discord")).setExecutor(new DiscordCommand());
        getCommand(Main.commands.getString("Dispose")).setExecutor(new DisposeCommand());
        getCommand(Main.commands.getString("Enderchest")).setExecutor(new EnderChestCommand());
        getCommand(Main.commands.getString("Extinguish")).setExecutor(new ExtinguishCommand());
        getCommand(Main.commands.getString("Feed")).setExecutor(new FeedCommand());
        getCommand(Main.commands.getString("Fly")).setExecutor(new FlyCommand());
        getCommand(Main.commands.getString("FlySpeed")).setExecutor(new FlySpeedCommand());
        getCommand(Main.commands.getString("Forums")).setExecutor(new ForumsCommand());
        getCommand(Main.commands.getString("Freeze")).setExecutor(new FreezeCommand());
        getCommand(Main.commands.getString("Gamemode")).setExecutor(new GamemodeCommand());
        getCommand(Main.commands.getString("God")).setExecutor(new GodCommand());
        getCommand(Main.commands.getString("Grindstone")).setExecutor(new GrindstoneCommand());
        getCommand(Main.commands.getString("Hat")).setExecutor(new HatCommand());
        getCommand(Main.commands.getString("Heal")).setExecutor(new HealCommand());
        getCommand(Main.commands.getString("Head")).setExecutor(new HeadCommand());
        getCommand(Main.commands.getString("Info")).setExecutor(new ItemInfoCommand());
        getCommand(Main.commands.getString("Loom")).setExecutor(new LoomCommand());
        getCommand(Main.commands.getString("More")).setExecutor(new MoreCommand());
        getCommand(Main.commands.getString("Near")).setExecutor(new NearCommand());
        getCommand(Main.commands.getString("Pony")).setExecutor(new PonyCommand());
        getCommand(Main.commands.getString("PrivateWarp")).setExecutor(new PrivateWarpCommand());
        getCommand(Main.commands.getString("RTP")).setExecutor(new RandomTPCommand());
        getCommand(Main.commands.getString("Repair")).setExecutor(new RepairCommand());
        getCommand(Main.commands.getString("SetFirstSpawn")).setExecutor(new FirstSpawnSetCommand());
        getCommand(Main.commands.getString("SetSpawn")).setExecutor(new SpawnSetCommand());
        getCommand(Main.commands.getString("Signedit")).setExecutor(new SignEditorCommand());
        getCommand(Main.commands.getString("Spawn")).setExecutor(new SpawnCommand());
        getCommand(Main.commands.getString("StoneCutter")).setExecutor(new StonecutterCommand());
        getCommand(Main.commands.getString("Sudo")).setExecutor(new SudoCommand());
        getCommand(Main.commands.getString("Suicide")).setExecutor(new SuicideCommand());
        getCommand(Main.commands.getString("Top")).setExecutor(new TopCommand());
        getCommand(Main.commands.getString("Tp")).setExecutor(new TeleportCommand());
        getCommand(Main.commands.getString("Tpa")).setExecutor(new TpaCommand());
        getCommand(Main.commands.getString("TpAccept")).setExecutor(new TpacceptCommand());
        getCommand(Main.commands.getString("TpAll")).setExecutor(new TpallCommand());
        getCommand(Main.commands.getString("TpDeny")).setExecutor(new TpdenyCommand());
        getCommand(Main.commands.getString("TpHere")).setExecutor(new TPhereCommand());
        getCommand(Main.commands.getString("Tppos")).setExecutor(new TPPosCommand());
        getCommand(Main.commands.getString("Twitter")).setExecutor(new TwitterCommand());
        getCommand(Main.commands.getString("Vanish")).setExecutor(new VanishCommand());
        getCommand(Main.commands.getString("WalkSpeed")).setExecutor(new WalkSpeedCommand());
        getCommand(Main.commands.getString("Warp")).setExecutor(new WarpCommand());
        getCommand(Main.commands.getString("WarpEdit")).setExecutor(new WarpEditCommand());
        getCommand(Main.commands.getString("Website")).setExecutor(new WebsiteCommand());
        getCommand(Main.commands.getString("Whois")).setExecutor(new WhoIsCommand());
        getCommand(Main.commands.getString("World")).setExecutor(new WorldCommand());
        getCommand(Main.commands.getString("Workbench")).setExecutor(new WorkbenchCommand());
        getCommand(Main.commands.getString("Youtube")).setExecutor(new YoutubeCommand());
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinQuit(), this);
        pm.registerEvents(new EquipEvents(), this);
        pm.registerEvents(new RespawnEvent(), this);
        pm.registerEvents(teleportHandler, this);
        AFKHandler.INSTANCE.registerTicker();
        pm.registerEvents(new MiscHandler(), this);
        pm.registerEvents(nmsImpl.getSignEditor(), this);
        //pm.registerEvents(new Databases(), this);
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
                .setReloadSettings(ReloadSettings.AUTOMATICALLY).setDataType(DataType.SORTED).createConfig();

        ////////////////
        // config.yml
        ////////////////
        mainConfig = LightningBuilder.fromFile(new File("plugins/XiavicCore/config"))
                .addInputStreamFromResource("config.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY).setDataType(DataType.SORTED).createConfig();

        ////////////////
        // commands.yml
        ////////////////
        commands = LightningBuilder.fromFile(new File("plugins/XiavicCore/Resources/commands"))
                .addInputStreamFromResource("commands.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY).setDataType(DataType.SORTED).createConfig();

        ////////////////
        // database.yml
        ////////////////
        database = LightningBuilder.fromFile(new File("plugins/XiavicCore/Resources/database.yml"))
                .addInputStreamFromResource("database.yml.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY).setDataType(DataType.SORTED).createConfig();

        //     ////////////////
        //     // players.json
        //     ////////////////
        //     Json players = new Json("players", Bukkit.getServer().getWorldContainer() + "/plugins/XiavicCore/Resources");
        //     players.set("players." + "NightPotato.Rank", "ThatNewGuy!");


    }

    private void loadshit() {
        saveResource("Resources/permissions.yml", false);
        saveResource("Resources/messages.yml", false);
        saveResource("Resources/commands.yml", false);
        saveResource("config.yml", false);
    }

    // I am using this function for updating the configs from the files inside the current
    // build of the plugin and preserves the spawn location in the mainConfig
    public void updateShit() {
        String firstspawnLocation = mainConfig.getString("FirstSpawn");
        String spawnLocation = mainConfig.getString("Spawn");
        saveResource("Resources/permissions.yml", true);
        saveResource("Resources/messages.yml", true);
        saveResource("Resources/commands.yml", true);
        saveResource("config.yml", true);
        mainConfig.set("FirstSpawn", firstspawnLocation);
        mainConfig.set("Spawn", spawnLocation);
        saveConfig();
    }

}
