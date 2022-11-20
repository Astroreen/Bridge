package bridge.config;

import bridge.Bridge;
import org.jetbrains.annotations.NotNull;

public interface Module {

    /**
     * Start module
     */
    boolean start(final @NotNull Bridge plugin);

    /**
     * Reload module
     */
    void reload();

    /**
     * End module
     */
    void disable();

    /**
     * Are all conditions met before starting module.
     *
     * @return true if all conditions met
     */
    boolean isConditionsMet();

    /**
     * Is module active right now.
     *
     * @return true if it is
     */
    boolean active();
}
