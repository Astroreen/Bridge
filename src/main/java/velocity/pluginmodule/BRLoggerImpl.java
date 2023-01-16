package velocity.pluginmodule;

import bridge.pluginmodule.logger.TopicLogger;
import common.logger.BRLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;


/**
 * This is the implementation of the interface {@link BRLogger}.
 */
public class BRLoggerImpl implements BRLogger {

    /**
     * The original logger.
     */
    private final Logger logger;

    /**
     * The topic.
     */
    private final String topic;

    /**
     * Creates a decorator for the {@link TopicLogger}.
     *
     * @param parentLogger The parent logger for this logger.
     * @param topic        The topic of the logger.
     */
    public BRLoggerImpl(final @NotNull Logger parentLogger, final String topic) {
        this.logger = parentLogger;
        this.topic = topic == null ? "" : topic;
    }
    @Override
    public void info(final @NotNull String msg) {
        logger.info(topic + msg);
    }

    @Override
    public void warn(final @NotNull String msg) {
        logger.warn(topic + msg);
    }

    @Override
    public void warn(final @NotNull String msg, final @NotNull Throwable thrown) {
        logger.warn(topic + msg, thrown);
    }

    @Override
    public void error(final @NotNull String msg) {
        logger.error(topic + msg);
    }

    @Override
    public void error(final @NotNull String msg, final @NotNull Throwable thrown) {
        logger.error(topic + msg, thrown);
    }

    @Override
    public void debug(final @NotNull String msg) {
        logger.debug(topic + msg);
    }

    @Override
    public void debug(final @NotNull String msg, final @NotNull Throwable thrown) {
        logger.debug(topic + msg, thrown);
    }

    @Override
    public void reportException(final @NotNull Throwable thrown) {
        final String msg = "This is an exception that should never occur.";
        logger.error(topic + msg, thrown);
    }
}
