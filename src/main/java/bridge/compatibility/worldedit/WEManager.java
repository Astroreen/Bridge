package bridge.compatibility.worldedit;

import bridge.Bridge;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@CustomLog
public class WEManager {

    private static Bridge plugin;
    private static boolean isEnabled;

    public static void setup(final @NotNull Bridge plugin) {
        WEManager.plugin = plugin;
        isEnabled = true;
    }

    /**
     * Pastes schematic to a given location.
     *
     * @param location the location to paste
     * @param folder the folder to get schematic from
     * @param name     the schematic name <p>
     *                 (ex: random-schematic.schem)
     * @return false, if cant find file. Otherwise, true.
     */
    public static boolean pasteSchematicAsync(final @NotNull Location location, final @NotNull File folder, final @NotNull String name) {
        if (!name.matches("^[A-Z\\-.+_()a-z1-9]+\\.schem")) return false;

        //get and check if schematic exists
        final File schematic = new File(folder, name);
        if (!schematic.exists()) return false;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            if (format == null) return;
            LOG.debug("Found schematic - " + name);

            final Clipboard[] clipboard = new Clipboard[1];
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
                clipboard[0] = reader.read();
            } catch (IOException e) {
                LOG.error("The exception occurred while tried to asynchronously paste schematic.", e);
            }

            if (location.getBlockX() == 0 && location.getBlockY() == 0 && location.getBlockZ() == 0) {
                LOG.debug("Schematic location is not set correctly. "
                        + "Can't paste schematic '" + name + "'");
                return;
            }

            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(location.getWorld());
            EditSession session = WorldEdit.getInstance().newEditSession(adaptedWorld);
            Operation operation = new ClipboardHolder(clipboard[0])
                    .createPaste(session)
                    .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
            session.close();
        });
        return true;
    }

    /**
     * Get default FastAsyncWorldEdit's folder
     * that contains schematics as {@link File}.
     *
     * @return path as file or null if plugin isn't enabled
     */
    @Contract(" -> new")
    public static @Nullable File getDefaultSchematicFolder() {
        if (isEnabled()) return null;
        return new File(plugin.getDataFolder().toPath()
                .resolve("../FastAsyncWorldEdit/schematics").toString());
    }

    public static void disable() {
        isEnabled = false;
    }

    public static boolean isEnabled() {
        return isEnabled;
    }
}
