package bridge.compatibility.itemsadder;

import bridge.compatibility.Integrator;

public class BRItemsAdderIntegrator implements Integrator {
    @Override
    public void hook() {
        new IAManager();
    }

    @Override
    public void reload() {
        IAManager.getInstance().reload();
    }

    @Override
    public void close() {
        //empty
    }
}
