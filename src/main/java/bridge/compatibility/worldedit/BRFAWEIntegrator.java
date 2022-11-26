package bridge.compatibility.worldedit;

import bridge.Bridge;
import bridge.compatibility.Integrator;

public class BRFAWEIntegrator implements Integrator {
    @Override
    public void hook(){
        WEManager.setup(Bridge.getInstance());
    }

    @Override
    public void reload() {

    }

    @Override
    public void close() {
        WEManager.disable();
    }
}
