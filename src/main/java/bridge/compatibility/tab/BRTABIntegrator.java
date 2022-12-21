package bridge.compatibility.tab;

import bridge.compatibility.Integrator;

public class BRTABIntegrator implements Integrator {

    @Override
    public void hook() {
        new TABManager().setup();
    }

    @Override
    public void reload() {

    }

    @Override
    public void close() {

    }
}
