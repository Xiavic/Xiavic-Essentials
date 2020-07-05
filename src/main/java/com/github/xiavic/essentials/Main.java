package com.github.xiavic.essentials;

// import com.github.xiavic.essentials.Utils.Misc.Databases;

import co.aikar.commands.*;
import com.github.xiavic.essentials.Commands.player.Essential.*;
import com.github.xiavic.essentials.Commands.player.Essential.Teleport.TeleportationCommandHandler;
import com.github.xiavic.essentials.Commands.player.Fun.*;
import com.github.xiavic.essentials.Commands.staff.cheats.CheatArmor;
import com.github.xiavic.essentials.Commands.staff.cheats.CheatEXP;
import com.github.xiavic.essentials.Commands.staff.noncheat.*;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TPPosCommand;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TPhereCommand;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TeleportCommand;
import com.github.xiavic.essentials.Commands.staff.noncheat.teleport.TpallCommand;
import com.github.xiavic.essentials.Utils.CommandBooleanValue;
import com.github.xiavic.essentials.Utils.EquipAnything.EquipEvents;
import com.github.xiavic.essentials.Utils.Listeners.*;
import com.github.xiavic.essentials.Utils.Tpa.TpaHandler;
import com.github.xiavic.essentials.Utils.Utils;
import com.github.xiavic.essentials.Utils.messages.CommandMessages;
import com.github.xiavic.essentials.Utils.messages.Messages;
import com.github.xiavic.essentials.Utils.messages.TeleportationMessages;
import com.github.xiavic.lib.NMSHandler.NMS;
import com.github.xiavic.lib.NMSHandler.NMSVersion;
import com.github.xiavic.lib.inventory.InventorySerializer;
import com.github.xiavic.lib.signedit.ISignEditor;
import com.github.xiavic.lib.teleport.ITeleportHandler;
import com.github.xiavic.lib.teleport.ITeleportRequestHandler;
import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;


public final class Main extends JavaPlugin {

    public static Yaml permissions;
    public static Yaml messages;
    public static Messages messages_new;
    public static Yaml mainConfig;
    public static Yaml commands;
    public static Yaml database;
    public static TpaHandler tpaHandler;
    public static TeleportHandler teleportHandler;
    public static NMS nmsImpl; //Should never be null after plugin init has completed.
    private static Main instance;
    private BukkitCommandManager commandManager;

    // Handle Instance of plugin in multiple classes.
    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        commandManager = new BukkitCommandManager(this);
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

    @Override
    public void onDisable() {

    }

    private void registerCommandsUtils() {
        commandManager.getCommandCompletions().registerCompletion("vararg_players", context -> {
            String[] input = context.getInput().split(" ");
            final int toPop;
            try {
                toPop = Integer.parseInt(context.getConfig("pop", "0"));
            } catch (final NumberFormatException ex) {
                ex.printStackTrace();
                return Collections.emptyList();
            }
            if (toPop > input.length) {
                throw new IllegalArgumentException(
                    "Config to pop is greater than input length!");
            }
            input = Arrays.copyOfRange(input, toPop, input.length - 1);
            for (int index = 0; index < input.length; index++) {
                input[index] = input[index].toLowerCase();
            }
            final List<String> players = new ArrayList<>(Arrays.asList(input));
            if (context.getPlayer() != null && !context.getConfig("self", "false")
                .equalsIgnoreCase("true")) {
                players.remove(context.getPlayer().getName());
            }
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                .filter(player -> !players.contains(player.toLowerCase()))
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        });
        commandManager.getCommandCompletions().registerCompletion("toggles", context -> {
            final String type = context.getConfig("type", "null");
            switch (type.toLowerCase()) {
                case "true":
                    return CommandBooleanValue.TRUE_VALUES;
                case "false":
                    return CommandBooleanValue.FALSE_VALUES;
                default:
                    return CommandBooleanValue.ALL_VALUES;
            }
        });
        commandManager.getCommandContexts().registerContext(CommandBooleanValue.class,
            context ->
                CommandBooleanValue.fromString(context.popFirstArg()).orElseThrow(
                    InvalidCommandArgument::new));  //TODO add message
        commandManager.setFormat(MessageType.ERROR,
            new BukkitMessageFormatter(ChatColor.RED, ChatColor.GOLD, ChatColor.WHITE) {
                @Override public String format(final String message) {
                    return ChatColor
                        .translateAlternateColorCodes('&', messages_new.messagePrefix.toString())
                        + ChatColor.RED + super.format(message);
                }
            });
        commandManager.getLocales().addMessage(Locale.ENGLISH, MinecraftMessageKeys.NO_PLAYER_FOUND, messages_new.messageNoPlayerFound.toString());
    }

    // TODO: Check if Object is not Null so we can catch exceptions!
    private void registerCommands() {
        //getCommand("fireball").setExecutor(new FireBallCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Argh"))).setExecutor(new ArghCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Afk"))).setExecutor(new AFKCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Back"))).setExecutor(new BackCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Cartography"))).setExecutor(new CartographyCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("CheatArmor"))).setExecutor(new CheatArmor());
        Objects.requireNonNull(getCommand(Main.commands.getString("CheatEXP"))).setExecutor(new CheatEXP());
        Objects.requireNonNull(getCommand(Main.commands.getString("Clear"))).setExecutor(new ClearCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("ClearAll"))).setExecutor(new ClearAllCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Coinflip"))).setExecutor(new CoinFlipCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Coreconfigupdate"))).setExecutor(new ConfigReloadCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Coreversion"))).setExecutor(new CoreVersionCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Discord"))).setExecutor(new DiscordCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Dispose"))).setExecutor(new DisposeCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Enderchest"))).setExecutor(new EnderChestCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Extinguish"))).setExecutor(new ExtinguishCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Feed"))).setExecutor(new FeedCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Fly"))).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("FlySpeed"))).setExecutor(new FlySpeedCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Forums"))).setExecutor(new ForumsCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Freeze"))).setExecutor(new FreezeCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Gamemode"))).setExecutor(new GamemodeCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("God"))).setExecutor(new GodCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Grindstone"))).setExecutor(new GrindstoneCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Hat"))).setExecutor(new HatCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Heal"))).setExecutor(new HealCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Head"))).setExecutor(new HeadCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Info"))).setExecutor(new ItemInfoCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Intantrespawn"))).setExecutor(new InstantRespawnCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Loom"))).setExecutor(new LoomCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("More"))).setExecutor(new MoreCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Near"))).setExecutor(new NearCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Pony"))).setExecutor(new PonyCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("PrivateWarp"))).setExecutor(new PrivateWarpCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("RTP"))).setExecutor(new RandomTPCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Repair"))).setExecutor(new RepairCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("SetFirstSpawn"))).setExecutor(new FirstSpawnSetCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("SetSpawn"))).setExecutor(new SpawnSetCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Signedit"))).setExecutor(new SignEditorCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Spawn"))).setExecutor(new SpawnCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("StoneCutter"))).setExecutor(new StonecutterCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Sudo"))).setExecutor(new SudoCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Suicide"))).setExecutor(new SuicideCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Top"))).setExecutor(new TopCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Tp"))).setExecutor(new TeleportCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Tpa"))).setExecutor(new TpaCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("TpAccept"))).setExecutor(new TpacceptCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("TpAll"))).setExecutor(new TpallCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("TpDeny"))).setExecutor(new TpdenyCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("TpHere"))).setExecutor(new TPhereCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Tppos"))).setExecutor(new TPPosCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Twitter"))).setExecutor(new TwitterCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Vanish"))).setExecutor(new VanishCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("WalkSpeed"))).setExecutor(new WalkSpeedCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Warp"))).setExecutor(new WarpCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("WarpEdit"))).setExecutor(new WarpEditCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Website"))).setExecutor(new WebsiteCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("Whois"))).setExecutor(new WhoIsCommand());
        Objects.requireNonNull(getCommand(Main.commands.getString("World"))).setExecutor(new WorldCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Workbench"))).setExecutor(new WorkbenchCommand());
        //Objects.requireNonNull(getCommand(Main.commands.getString("Youtube"))).setExecutor(new YoutubeCommand());

        //Modern Commands:
        new TeleportationCommandHandler(commandManager, teleportHandler, tpaHandler);
        new EssentialCommandHandler(commandManager);
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
            if (NMSVersion.getCurrent().isOlderThan(NMSVersion.V1_14_R1)) { //We only support 1.14 and onwards.
                final String message = messages_new.messageUnsupportedServerVersion.toString();
                getLogger().log(Level.SEVERE,
                    Utils.chat(message.replace("%version%", Bukkit.getVersion())));
                return false;
            }
            try {
                final Class<?> clazz = Class
                        .forName("com.github.xiavic.essentials" + Utils.parseNMSVersion() + ".NMSImpl");
                final Class<? extends NMS> nmsImplClass = clazz.asSubclass(NMS.class);
                Main.nmsImpl = nmsImplClass.getDeclaredConstructor().newInstance();
            } catch (final ReflectiveOperationException ex) {
                ex.printStackTrace();
                getLogger().log(Level.SEVERE, Utils.chat("&cFailed to register NMS implementation! See error above."));
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
                        .setReloadSettings(ReloadSettings.AUTOMATICALLY)
                        .setDataType(DataType.SORTED)
                        .createConfig();

        ////////////////
        // messages.yml
        ////////////////
        final Config messageFile = (LightningBuilder.fromFile(new File("plugins/XiavicCore/Resources/messages"))
                .addInputStreamFromResource("messages.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY)
                .setDataType(DataType.SORTED)
                .createConfig());
        messages = messageFile;
        messages_new.load(messageFile);
        TeleportationMessages.INSTANCE.load(messageFile);
        CommandMessages.INSTANCE.load(messageFile);

        ////////////////
        // config.yml
        ////////////////
        mainConfig = LightningBuilder.fromFile(new File("plugins/XiavicCore/config"))
                .addInputStreamFromResource("config.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY)
                .setDataType(DataType.SORTED)
                .createConfig();

        ////////////////
        // commands.yml
        ////////////////
        commands = LightningBuilder.fromFile(new File("plugins/XiavicCore/Resources/commands"))
                .addInputStreamFromResource("commands.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY)
                .setDataType(DataType.SORTED)
                .createConfig();

        ////////////////
        // database.yml
        ////////////////
        database = LightningBuilder.fromFile(new File("plugins/XiavicCore/Resources/database.yml"))
                .addInputStreamFromResource("database.yml.yml")
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.AUTOMATICALLY)
                .setDataType(DataType.SORTED)
                .createConfig();

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
