package common;

import bridge.Bridge;
import common.exceptions.HookException;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for modules.
 * <br><br/>
 * <i><b>Note:</b> use constructors for
 * one time actions at the early start.</i>
 */
public interface IModule {

    /**
     * Start module. Can start multiple times,
     * use constructors for one time actions
     * at the early start.
     *
     * @throws HookException when you can't start method.
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
     * Get name of the module.
     *
     * @return name of module
     */
    default String getName() {
        final String[] name = getClass().getName().split("\\.");
        return name[name.length - 1];
    }
}
