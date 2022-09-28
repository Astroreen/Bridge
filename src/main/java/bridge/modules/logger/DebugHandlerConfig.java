package bridge.modules.logger;

import bridge.config.ConfigurationFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Handler;

public class DebugHandlerConfig {

    /**
     * The config path that holds all debug configuration settings..
     */
    private static final String CONFIG_SECTION = "debug";

    /**
     * The full path to the config setting, that saved if debugging is enabled.
     */
    private static final String CONFIG_ENABLED_PATH = CONFIG_SECTION + ".enabled";

    private static boolean DEBUGGING;

    /**
     * The {@link ConfigurationFile} where to configure debugging.
     */
    private final ConfigurationFile config;

    /**
     * Wrap the given {@link ConfigurationFile} to easily access the relevant options for the debug {@link Handler}.
     *
     * @param config the related {@link ConfigurationFile}
     */
    public DebugHandlerConfig(final @NotNull ConfigurationFile config) {
        this.config = config;
        DEBUGGING = config.getBoolean(CONFIG_ENABLED_PATH, false);
    }

    /**
     * Get logging state.
     *
     * @return true if debugging is enabled in the config; false otherwise
     */
    public boolean isDebugging() {
        return DEBUGGING;
    }

    /**
     * Set logging state.
     *
     * @param debug enabled state to set
     * @throws IOException when persisting the changed state fails
     */
    public void setDebugging(final boolean debug) throws IOException {
        if (!config.isBoolean(CONFIG_ENABLED_PATH) || config.getBoolean(CONFIG_ENABLED_PATH) != debug) {
            config.set(CONFIG_ENABLED_PATH, debug);
            config.save();
            DEBUGGING = debug;
        }
    }
}
