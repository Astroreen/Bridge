package bridge.compatibility.tab;

import bridge.compatibility.Integrator;
import bridge.modules.Module;
import bridge.modules.ModuleManager;

public class BRTABIntegrator implements Integrator {

    private final static String MODULE_NAME = "TAB";
    @Override
    public void hook() {
        TABManager.setup();
    }

    @Override
    public void reload() {
        Module module = ModuleManager.getModule(MODULE_NAME);
        if (module != null && module.active()) module.reload();
    }

    @Override
    public void close() {
        Module module = ModuleManager.getModule(MODULE_NAME);
        if (module != null && module.active()) module.disable();
    }
}
