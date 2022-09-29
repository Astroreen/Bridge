package bridge.config;

import bridge.modules.config.ConfigurationFileImpl;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ConfigurationFile extends ConfigurationSection {

    /**
     * Uses {@link ConfigAccessor#create(File, Plugin, String)} to either load or create a {@link ConfigurationFile}.
     * <br>
     * Additionally, attempts to patch the {@code configurationFile} with a patch file.
     * This patch file must exist in the same directory as the {@code resourceFile}.
     * Its color is the one of the {@code resourceFile} but with
     * '.patch' inserted between the file color and the file extension.
     * <br>
     * E.g:
     * {@code  config.yml & config.patch.yml}
     *
     * @param configurationFile where to load and save the config
     * @param plugin            to load the jar resources from
     * @param resourceFile      path to the default config in the plugin's jar
     * @return a new ConfigurationFile
     * @throws InvalidConfigurationException if the configuration is invalid or could not be saved
     * @throws FileNotFoundException         if the {@code configurationFile} or {@code resourceFile} could not be found
     */
    @Contract("null, _, _ -> fail; !null, null, _ -> fail; !null, !null, null -> fail")
    static @NotNull ConfigurationFile create(final File configurationFile, final Plugin plugin, final String resourceFile)
            throws InvalidConfigurationException, FileNotFoundException {
        return ConfigurationFileImpl.create(configurationFile, plugin, resourceFile);
    }

    /**
     * Saves the file that is represented by this {@link ConfigAccessor}.
     * This method does nothing if no configurationFile was provided in the constructor.
     *
     * @return Only returns true if the file was saved.
     * @throws IOException if the file could not be saved.
     */
    boolean save() throws IOException;

    /**
     * Delete the file that is represented by this {@link ConfigAccessor}.
     * This method does nothing if no configurationFile was provided in the constructor.
     *
     * @return Only returns true if the file was deleted and existed before.
     * @throws IOException if the file could not be deleted.
     */
    boolean delete() throws IOException;

    /**
     * Reloads from the file that is represented by this {@link ConfigAccessor}.
     * This method does nothing if no configurationFile was provided in the constructor.
     *
     * @return Only returns true if the file was successfully reloaded.
     * @throws IOException if the file could not be reloaded.
     */
    boolean reload() throws IOException;
}
