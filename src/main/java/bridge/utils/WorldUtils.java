package bridge.utils;

import bridge.Bridge;
import bridge.world.generator.voidgen.VoidChunkGen;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class WorldUtils {

    public static @Nullable World loadWorld(final @NotNull String name) {
        if (!isWorldFolderExist(name)) return null;
        if (isWorldLoaded(name)) return getWorld(name);
        return new WorldCreator(name).createWorld();
    }

    public static boolean unloadWorld(final @NotNull String name, final boolean save) {
        if (isWorldFolderExist(name)) {
            if (!isWorldLoaded(name)) return true;
            World world = getWorld(name);
            if (world == null) return true;
            return Bukkit.getServer().unloadWorld(world, save);
        } else return false;
    }

    public static boolean unloadWorld(final @NotNull World world, final boolean save) {
        return Bukkit.getServer().unloadWorld(world, save);
    }

    public static World createEmptyWorld(final @NotNull String name) {
        return new WorldCreator(name)
                .generator(new VoidChunkGen(Bridge.getInstance(), UUID.randomUUID().toString()))
                .createWorld();
    }

    @Contract(pure = true)
    public static @Nullable World getWorld(final @NotNull String name) {
        return Bukkit.getWorld(name);
    }

    public static boolean isWorldFolderExist(final @NotNull String name) {
        return new File(Bukkit.getServer().getWorldContainer(), name).exists();
    }

    public static @NotNull List<World> getAllWorlds() {
        return Bukkit.getWorlds();
    }

    public static boolean isWorldLoaded(final @NotNull String name) {
        return getWorld(name) != null;
    }
}
