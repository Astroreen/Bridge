package bridge.ffa;

import bridge.config.ConfigurationFile;
import org.jetbrains.annotations.NotNull;

public class FFAKitManager {

    private static ConfigurationFile config;

    public static void setup(final @NotNull ConfigurationFile config){
        FFAKitManager.config = config;
    }

    private void pasteToConfig(final @NotNull FFAKit kit){

    }
}
