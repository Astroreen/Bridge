package bridge.modules.logger;

import bridge.Bridge;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the implementation of the interface {@link BRLogger}.
 */
public class BRLoggerImpl implements BRLogger {

    /**
     * The {@link Plugin} this logger belongs to.
     */
    private final Plugin plugin;

    /**
     * The original logger.
     */
    private final Logger logger;
    private boolean debug = true;
    private DebugHandlerConfig debugHandlerConfig;

    /**
     * Creates a decorator for the {@link TopicLogger}.
     *
     * @param plugin       The {@link Plugin} this logger belongs to.
     * @param parentLogger The parent logger for this logger.
     * @param clazz        The calling class.
     * @param topic        The topic of the logger.
     */
    public BRLoggerImpl(@NotNull final Plugin plugin, final Logger parentLogger, final Class<?> clazz, final String topic) {
        this.plugin = plugin;
        this.logger = new TopicLogger(parentLogger, clazz, topic);
        if(Bridge.getInstance().isConfigSet()){
            debugHandlerConfig = new DebugHandlerConfig(Bridge.getInstance().getPluginConfig());
            debug = debugHandlerConfig.isDebugging();
        }
    }

    public void reload(){
        if(debugHandlerConfig == null && !Bridge.getInstance().isConfigSet()){
            debugHandlerConfig = new DebugHandlerConfig(Bridge.getInstance().getPluginConfig());
            debug = debugHandlerConfig.isDebugging();
        }
    }

    @Override
    public void info(final String msg) {
        final BRLogRecord record = new BRLogRecord(Level.INFO, msg, plugin.getName());
        logger.log(record);
    }

    @Override
    public void warn(final String msg) {
        warn(msg, null);
    }

    @Override
    public void warn(final String msg, final Throwable thrown) {
        final BRLogRecord record = new BRLogRecord(Level.WARNING, msg, plugin.getName());
        record.setThrown(thrown);
        logger.log(record);
    }

    @Override
    public void error(final String msg) {
        error(msg, null);
    }

    @Override
    public void error(final String msg, final Throwable thrown) {
        final BRLogRecord record = new BRLogRecord(Level.SEVERE, msg, plugin.getName());
        record.setThrown(thrown);
        logger.log(record);
    }

    @Override
    public void debug(final String msg) {
        debug(msg, null);
    }

    @Override
    public void debug(String msg, Throwable thrown) {
        if(!debug) return;
        final BRLogRecord record = new BRLogRecord(Level.FINE, msg, plugin.getName());
        record.setThrown(thrown);
        logger.log(record);
    }

    @Override
    public void reportException(Throwable thrown) {
        final String msg = "This is an exception that should never occur.";
        final BRLogRecord record = new BRLogRecord(Level.SEVERE, msg, plugin.getName());
        record.setThrown(thrown);
        logger.log(record);
    }
}
