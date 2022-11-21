package bridge.compatibility.itemsadder;

import bridge.compatibility.Integrator;

public class BRItemsAdderIntegrator implements Integrator {
    @Override
    public void hook() {
        new ItemsAdderManager();
    }

    @Override
    public void reload() {
        ItemsAdderManager.getInstance().reload();
    }

    @Override
    public void close() {
        //empty
    }
}
