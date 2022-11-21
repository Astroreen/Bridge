package bridge.ffa;

import bridge.config.ConfigurationFile;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FFAKitManager {

    private static ConfigurationFile config;

    public FFAKitManager(final @NotNull ConfigurationFile config){
        FFAKitManager.config = config;
    }

    public void createKitItem (ItemStack stack){
    }
    private void pasteToConfig(final @NotNull FFAKitItem kit){

    }
}
