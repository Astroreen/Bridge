package bridge;

import bridge.commands.BridgeCommand;
import bridge.compatibility.Compatibility;
import bridge.config.Config;
import bridge.config.ConfigurationFile;
import bridge.config.ModuleManager;
import bridge.database.AsyncSaver;
import bridge.database.Database;
import bridge.database.MySQL;
import bridge.database.SQLite;
import bridge.listeners.ListenerManager;
import bridge.modules.logger.BRLogger;
import bridge.modules.logger.DebugHandlerConfig;
import bridge.modules.messenger.Action;
import bridge.modules.messenger.Messenger;
import bridge.modules.permissions.PermissionManager;
import bridge.utils.StartScreen;
import lombok.Getter;
import me.clip.placeholderapi.libs.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public final class Bridge extends JavaPlugin {

    /**
     * The Bridge Plugin instance.
     * <p>
     * -- GETTER --
     * <p>
     * Get the plugin's instance.
     */
    @Getter
    private static Bridge instance;
    private static BRLogger log;
    private ListenerManager listenerManager;

    /**
     * The adventure instance.
     * <p>
     * -- GETTER --
     * <p>
     * Get the adventure instance.
     */
    @Getter
    private BukkitAudiences adventure;
    private ConfigurationFile config;
    private Database database;
    private AsyncSaver saver;
    private Messenger messenger;
    private PermissionManager perms;
    private boolean isMySQLUsed;

    @NotNull
    public ConfigurationFile getPluginConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        log = BRLogger.create(this);
        try {
            config = ConfigurationFile.create(new File(getDataFolder(), "config.yml"), this, "config.yml");
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            getLogger().log(Level.SEVERE, "Could not load the config.yml file!", e);
            return;
        }
        DebugHandlerConfig.setup(config);

        listenerManager = new ListenerManager();

        adventure = BukkitAudiences.create(this);
        Config.setup(this);

        // try to connect to database
        final boolean mySQLEnabled = config.getBoolean("mysql.enabled", true);
        if (mySQLEnabled) {
            log.debug("Connecting to MySQL database");
            this.database = new MySQL(this, config.getString("mysql.host"),
                    config.getString("mysql.port"),
                    config.getString("mysql.database"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            if (database.getConnection() != null) {
                isMySQLUsed = true;
                log.info("Successfully connected to MySQL database!");
            }
        }
        if (!mySQLEnabled || !isMySQLUsed) {
            this.database = new SQLite(this, "database.db");
            if (mySQLEnabled) {
                log.warn("No connection to the mySQL Database! Using SQLite for storing data as fallback!");
            } else {
                log.info("Using SQLite for storing data!");
            }
        }

        // create tables in the database
        database.createTables(isMySQLUsed);

        // create and start the saver object, which handles correct asynchronous
        // saving to the database
        saver = new AsyncSaver();
        saver.start();

        // initialize compatibility with other plugins
        new Compatibility();

        // initializing all modules
        ModuleManager.setup(this);
        ModuleManager.start();
        perms = new PermissionManager();

        // initialize commands
        new BridgeCommand();

        //registering plugin messenger
        if (config.getBoolean("settings.modules.updater.enabled", true)) {
            messenger = new Messenger(this);
            messenger.register();
            messenger.makeReservation(Action.GET_SERVER);
            messenger.makeReservation(Action.GET_SERVERS);
        } else messenger = null;

        // done
        new StartScreen(this.getServer().getConsoleSender()).BridgeImage();
        log.info("Bridge successfully enabled!");

        //refreshing db connection
        final long updateTime = config.getLong("mysql.updateTime", 30) * 1200;
        final Runnable runnable = () -> {
            try {
                database.getConnection().prepareStatement("SELECT 1").executeQuery().close();
            } catch (final SQLException e) {
                log.warn("Refreshing the database...", e);
            }
        };
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, runnable, updateTime, updateTime).getTaskId();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        ModuleManager.disable();
        Compatibility.disable();
        if (messenger != null) {
            messenger.unregister();
            messenger.getSender().end();
            messenger.stop();
        }

        Bukkit.getScheduler().cancelTasks(this);

        //close database connection
        if (saver != null) saver.end();
        if (database != null) database.closeConnection();

        // done
        log.info("Bridge successfully disabled!");

        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    /**
     * Returns the messenger instance
     *
     * @return {@link Messenger} instance
     */
    public @Nullable Messenger getMessenger() {
        return messenger;
    }

    /**
     * Returns the permission manager
     *
     * @return {@link Messenger} instance
     */
    public PermissionManager getPermManager() {
        return perms;
    }

    /**
     * Returns the manager instance
     *
     * @return {@link ListenerManager} instance
     */
    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    /**
     * Returns the database instance
     *
     * @return {@link Database} instance
     */
    public Database getDB() {
        return database;
    }

    public void reload() {
        // reload the configuration
        log.debug("Reloading configuration");
        try {
            config.reload();
        } catch (final IOException e) {
            log.warn("Could not reload config! " + e.getMessage(), e);
        }
        Config.setup(this);
        DebugHandlerConfig.setup(config);
        //registering plugin messenger
        if (messenger != null && config.getBoolean("settings.modules.updater.enabled", false))
            messenger.reload();
        Compatibility.reload();
        ModuleManager.reload(this);
        perms = new PermissionManager();
    }

    public AsyncSaver getSaver() {
        return saver;
    }

    /**
     * Get key in map from value that you will pass.
     *
     * @param map   your map what you want to get key from
     * @param value your key
     * @return one key from map
     */
    public static <T, E> @Nullable T getKeyByValue(@NotNull Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
