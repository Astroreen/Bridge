package bridge.compatibility.placeholderapi;

import bridge.compatibility.Integrator;

public class PlaceholderAPIIntegrator implements Integrator {

    @Override
    public void hook() {
        new BRPlaceholder().register();
    }

    @Override
    public void reload() {
        //Empty
    }

    @Override
    public void close() {
        //Empty
    }
}
