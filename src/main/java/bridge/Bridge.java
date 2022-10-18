package bridge;

import bridge.commands.BridgeCommand;
import bridge.compatibility.Compatibility;
import bridge.config.Config;
import bridge.config.ConfigurationFile;
import bridge.database.AsyncSaver;
import bridge.database.Database;
import bridge.database.MySQL;
import bridge.database.SQLite;
import bridge.modules.logger.BRLogger;
import lombok.Getter;
import me.clip.placeholderapi.libs.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

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
            log.reload();
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            log.error("Could not load the config.yml file!", e);
            return;
        }

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

        // initialize commands
        new BridgeCommand();

        // done
        log.info("Bridge successfully enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // cancel database saver
        if (saver != null) {
            saver.end();
        }
        Compatibility.disable();
        if (database != null) {
            database.closeConnection();
        }

        // done
        log.info("Bridge successfully disabled!");

        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    /**
     * Returns the database instance
     *
     * @return Database instance
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
        Compatibility.reload();
    }

    public AsyncSaver getSaver() {
        return saver;
    }

    public boolean isMySQLUsed () {
        return isMySQLUsed;
    }

    public boolean isConfigSet(){
        return config != null;
    }

    /**
     * Get key in map from value that you will pass.
     *
     * @param map your map what you want to get key from
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
