package bridge.ffa;

import bridge.config.ConfigurationFile;
import org.jetbrains.annotations.NotNull;

public class FFAKit {

    private static ConfigurationFile config;

    public static void setup(final @NotNull ConfigurationFile config){
        FFAKit.config = config;
    }


}
