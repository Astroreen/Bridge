package bridge.compatibility.tab;

import bridge.compatibility.Integrator;

public class BRTABIntegrator implements Integrator {

    private TABManager manager;

    @Override
    public void hook() {
        manager = new TABManager();
        manager.register();
    }

    @Override
    public void reload() {
        manager.reload();
    }

    @Override
    public void close() {
        manager.unregister();
    }
}
