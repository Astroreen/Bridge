package bridge.modules;

import bridge.Bridge;
import bridge.exceptions.HookException;
import org.jetbrains.annotations.NotNull;

public interface Module {

    /**
     * Start module
     */
    boolean start(final @NotNull Bridge plugin) throws HookException;

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

    /**
     * Get name of the module.
     *
     * @return name of module
     */
    default String getName() {
        final String[] name = getClass().getName().split("\\.");
        return name[name.length - 1];
    }
}
