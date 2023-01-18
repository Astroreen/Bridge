package bridge.compatibility.protocollib;

import bridge.compatibility.Integrator;
import common.exceptions.HookException;

public class BRProtocolLibIntegrator implements Integrator {
    @Override
    public void hook() throws HookException {
        ProtocolLibManager.setup();
    }

    @Override
    public void reload() {

    }

    @Override
    public void close() {
        ProtocolLibManager.disable();
    }
}
