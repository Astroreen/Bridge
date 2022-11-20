package bridge.ffa;

import bridge.Bridge;
import bridge.config.ConfigurationFile;
import bridge.config.Module;
import bridge.utils.FileUtils;
import lombok.CustomLog;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
@CustomLog(topic = "FFA")
public class FFA implements Module {

    private static Bridge plugin;
    private static ConfigurationFile config;
    private static ConfigurationFile kits;
    private static boolean isActive = false;

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        FFA.plugin = plugin;
        final String dir = "/ffa/";
        if(!FileUtils.createDirectory(dir)) {
            LOG.error("Wasn't able to create a directory for ffa module.");
            return false;
        }
        if(FileUtils.isValidPath("/schematics/")) FileUtils.createDirectory("/schematics/");
        final File root = new File(plugin.getDataFolder() + dir);
        try {
            FFA.config = ConfigurationFile.create(new File(root, "ffa-config.yml"), plugin, "ffa/ffa-config.yml");
            FFA.kits = ConfigurationFile.create(new File(root, "kits.yml"), plugin, "ffa/kits.yml");
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Wasn't able to create 'ffa-config.yml' file!", e);
            return false;
        }

        FFAKitManager.setup(kits);

        //fully working
        isActive = true;
        return true;
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {
        isActive = false;
    }

    @Override
    public boolean isConditionsMet() {
        return true;
    }

    @Override
    public boolean active() {
        return isActive;
    }
}
